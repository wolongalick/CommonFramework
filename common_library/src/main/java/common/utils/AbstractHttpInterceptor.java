package common.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 抽象的http拦截器
 * Created by cxw on 2016/9/30.
 */

public abstract class AbstractHttpInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private boolean enableDebug;

    private java.lang.String TAG = "AbstractHttpInterceptor";

    public AbstractHttpInterceptor(boolean enableDebug) {
        this.enableDebug = enableDebug;
    }


    public void addHeader(Request.Builder builder) {
        //默认空实现
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        addHeader(builder);
        Request request = builder.build();
        Response response = chain.proceed(request);


        RequestBody requestBody = request.body();

        if (enableDebug) {
            Headers headers = request.headers();
            int headerSize = headers.size();
            BLog.i("-----请求头-----");
            for (int i = 0; i < headerSize; i++) {
                BLog.i(headers.name(i) + ":" + headers.value(i));
            }

            BLog.i("-----请求参数-----");
            BLog.i("url:" + request.url());
            Charset charset = UTF8;
            if(requestBody!=null){
                BLog.i("Content-Type: " + requestBody.contentType());

                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                BLog.i(buffer.readString(charset));
            }



            BLog.i("-----响应参数-----");

//          响应体部分============================================

            ResponseBody responseBody = response.body();
            long contentLength = responseBody.contentLength();
            BLog.i("responseCode:" + response.code() + " " + response.message());

            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer responseBuffer = source.buffer();

            MediaType responseContentType = responseBody.contentType();
            if (responseContentType != null) {
                try {
                    charset = responseContentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    BLog.e(TAG,"Couldn't decode the response body; charset is likely malformed.");
                    BLog.e(TAG,"<-- END HTTP");

                    return response;
                }
            }

            if (contentLength != 0) {
                BLog.i(responseBuffer.clone().readString(charset));
            }
        }


        return response;
    }

     /*========================set/get方法-begin========================*/

    public void setEnableDebug(boolean enableDebug) {
        this.enableDebug = enableDebug;
    }

    public boolean isEnableDebug() {
        return enableDebug;
    }

    /*========================set/get方法-end========================*/
}
