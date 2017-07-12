package common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import common.bean.WanIp;

/**
 * Created by Admin on 2015/11/9.
 */
public class NetConnectionUtils {
    private static final String TAG = "NetConnectionUtils";

    /**
     * 判断手机当前是否联网
     * */
    public static boolean isNetWorkConn(Context context){
        ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();
        if(info!=null){
            return info.isConnected();
        }
        return false;
    }

    /**
     * 获取网络类型
     * @param context
     * @return
     */
    public static NetWorkType getNetworkType(Context context)
    {
        NetWorkType strNetworkType = NetWorkType.Net_2G;

        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                strNetworkType = NetWorkType.Net_WIFI;
            }
            else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                String _strSubTypeName = networkInfo.getSubtypeName();

                BLog.e(TAG, "Network getSubtypeName : " + _strSubTypeName);

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = NetWorkType.Net_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = NetWorkType.Net_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = NetWorkType.Net_4G;
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000"))
                        {
                            strNetworkType = NetWorkType.Net_3G;
                        }
                        break;
                }

                BLog.e(TAG,"Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }
        BLog.e(TAG,"Network Type : " + strNetworkType);
        return strNetworkType;
    }

    /**
     * 网络类型
     */
    public enum NetWorkType{
        Net_2G,Net_3G,Net_4G,Net_WIFI
    }

    /**
     * 获取网络名称
     * @param context
     * @return
     */
    public static String getNetworkName(Context context)
    {
        String strNetworkType = "";

        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                strNetworkType = "WIFI";
            }
            else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                String _strSubTypeName = networkInfo.getSubtypeName();

                Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000"))
                        {
                            strNetworkType = "3G";
                        }
                        else
                        {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }

                BLog.e(TAG, "Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }

        BLog.e(TAG, "Network Type : " + strNetworkType);

        return strNetworkType;
    }

    public interface INetIpListener{
        void onGetNetIp(String ip);
    }

    public static void getNetIp(Context context, final INetIpListener iNetIpListener) {
        if(!isNetWorkConn(context)){
            //若没有网络则直接返回空字符串
            iNetIpListener.onGetNetIp("");
        }

        new Thread(){
            @Override
            public void run() {
                String ip = "";
                try {
                    String address = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
                    URL url = new URL(address);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setUseCaches(false);

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream in = connection.getInputStream();
                        // 将流转化为字符串
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String tmpString = "";
                        StringBuilder retJSON = new StringBuilder();
                        while ((tmpString = reader.readLine()) != null) {
                            retJSON.append(tmpString + "\n");
                        }
                        JSONObject jsonObject = new JSONObject(retJSON.toString());
                        String code = jsonObject.getString("code");
                        if (code.equals("0")) {
                            WanIp wanIp=JsonUtils.parseJson2Bean(retJSON.toString(), WanIp.class);
                            BLog.i(TAG,"您的IP地址是：" + wanIp.getData().getIp());

                            ip=wanIp.getData().getIp();
                        }
                    }else {
                        BLog.i(TAG,"网络连接失败");
                    }
                } catch (Exception e) {
                    System.out.println("获取IP地址时出现异常，异常信息是：" + e.toString());
                }
                iNetIpListener.onGetNetIp(ip);
            }
        }.start();

    }

}
