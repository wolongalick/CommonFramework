package common.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.UnsupportedEncodingException;

/**
 * 用于执行js脚本,并能够接收返回值的工具类<br/>
 * 比如要计算(1+2)*5/3的值,
 * 只需要调用executeJs("(1+2)*5/3",iResultCallback);
 *
 */
public class JsUtils {
    private WebView webView;
    private static final String jsObjName="hahahahahah";    //js对象名随便写
    private static final String functionName="returnResult";//js调用java时,需要调用的方法名

    private IResultCallback IResultCallback;
    private Handler handler;

    public JsUtils(Context context) {
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                IResultCallback.onResult(msg.obj.toString());
            }
        };

        webView=new WebView(context);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this,jsObjName);
    }

    /**
     * 执行结果的回调接口
     */
    public interface IResultCallback {
        void onResult(String result);
    }

    @JavascriptInterface
    public void returnResult(double result) {
        handler.sendMessage(handler.obtainMessage(0,result+""));
    }

    /**
     * 执行脚本
     * @param javaScript
     * @param IResultCallback
     */
    public void executeJs(String javaScript,IResultCallback IResultCallback){
        this.IResultCallback = IResultCallback;
        loadJavaScript(jsObjName+"."+functionName+"(eval("+javaScript+"));");
    }

    /**
     * 加载脚本
     * @param javascript
     */
    private void loadJavaScript(String javascript) {
        try {
            javascript = "<script>" + javascript + "</script>";
            byte[] data = javascript.getBytes("UTF-8");
            String e = Base64.encodeToString(data, 0);
            webView.loadUrl("data:text/html;charset=utf-8;base64," + e);
        } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
        }
    }

}