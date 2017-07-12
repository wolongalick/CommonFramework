package common.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Created by acewill on 2016/9/21.
 */
public class BIUtils {

    /**
     * ******************* get Device ID *****************************
     */
    /*
     * 获取设别的唯一标志
     * 1）该唯一标志生成后需要存在本地文件中，以便后续获取使用
     * 2）以如下顺序来获取唯一标志
     * IMEI，缺点：对于PAD设备可能取不到
     * CPU序列号，缺点：该值需要从系统文件/proc/cpuinfo中获取，某些系统不存在该文件
     * Android2.3版本以上可以使用android.os.Build.SERIAL，缺点：有版本限制
     * Android2.2版本以上系统可以使用ANDROID_ID，注意“9774d56d682e549c”是无效ID，缺点：有版本限制
     * IMSI，缺点：必须插入SIM卡；对于CDMA设备，返回的是一个空值
     * MAC地址，缺点：用户如果未打开WIFI则获取不到
     * UUID，缺点：与设备无关，每次生成都不同，且长度为32位
     * 注意： 1. 需要过滤无效的ID，这些ID保存在assets/invalid-imei.idx中，支持正则表达式
     *       2. 除了IMEI号，其他方式取出的都用特殊前缀标明，例如：CPU添加C; SERIAL添加S; ANDROID_ID添加A; IMSI添加I; MAC添加M; UUID添加U
     */
    private final static String INVALID_IMEI_FILENAME = "invalid-imei.idx";
    private final static String DEVICE_ID_FILENAME_NEW = "DEV";
    private final static String ANDROID_ID_FILENAME = "ANDROID_ID"; //保存手机的android_id，用来校验DEVICE_ID是否需要重新获取
    private static String sDeviceId = null;
    private static String sNewDeviceId = null;


    /**
     * 获得DeviceId
     *
     * @param context
     * @return
     */
    public synchronized static String getDeviceId(Context context) {
        if (sDeviceId == null) {
            File newFile = new File(context.getFilesDir(), DEVICE_ID_FILENAME_NEW);
            if (newFile.exists()) {
                //新文件存在，直接读取
                sDeviceId = readIdFile(context, newFile, true);
                if (TextUtils.isEmpty(sDeviceId)) {
                    //可能读取失败，则重新生成
                    createDeviceId(context, newFile, true);
                }
            } else {
                createDeviceId(context, newFile, true);
            }
        }
        return (sDeviceId == null) ? "" : sDeviceId;
    }

    private static String readIdFile(Context context, File idFile, boolean decode) {
        RandomAccessFile f = null;
        String deviceId = null;
        try {
            f = new RandomAccessFile(idFile, "r");
            byte[] bytes = new byte[(int) f.length()];
            f.readFully(bytes);
            if (decode) {
                deviceId = AESUtils.AESDecrypt(new String(bytes) + context.getPackageName());
            } else {
                deviceId = new String(bytes);
            }
        } catch (Exception ex) {
        } finally {
            if (f != null) {
                try {
                    f.close();
                } catch (Exception ex) {
                }
            }
        }
        return deviceId;
    }

    private static void writeIdFile(Context context, String id, File idFile, boolean encode) {
        if (TextUtils.isEmpty(id))
            return;

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(idFile, false);
            if (encode) {
                id = AESUtils.AESEncrypt(id + context.getPackageName());
            }
            out.write(id.getBytes());
        } catch (Exception ex) {
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    private static void createDeviceId(Context context, File file, boolean encode) {
        try {
            String deviceId = null;
            // IMEI
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(
                    Context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();
            if (invalidDeviceId(context, deviceId)) {
                // CPU序列号
                deviceId = getCPUSerial();
                if (deviceId != null) {
                    deviceId = deviceId.toLowerCase();
                }
                if (invalidDeviceId(context, deviceId)) {
                    // android.os.Build.SERIAL
                    deviceId = getSerial();
                    if (invalidDeviceId(context, deviceId)) {
                        // ANDROID_ID
                        deviceId = getAndroidId(context);
                        if (invalidDeviceId(context, deviceId)) {
//                            // IMSI
//                            deviceId = getIMSI(context, 0);
                            if (invalidDeviceId(context, deviceId)) {
                                // MAC地址
                                deviceId = getMacAddress(context);
                                if (invalidDeviceId(context, deviceId)) {
                                    // UUID
                                    deviceId = "U" + getUUID();
                                } else {
                                    deviceId = "M" + deviceId;
                                }
                            } else {
                                deviceId = "I" + deviceId;
                            }
                        } else {
                            deviceId = "A" + deviceId;
                        }
                    } else {
                        deviceId = "S" + deviceId;
                    }
                } else {
                    deviceId = "C" + deviceId;
                }
            }
            sDeviceId = deviceId;
            writeIdFile(context, sDeviceId, file, encode);
        } catch (Exception ex) {
        }
    }

    private static boolean invalidDeviceId(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }

//        InputStream is = null;
//        try {
//            is = context.getAssets().open(INVALID_IMEI_FILENAME);
//            if (is != null) {
//                BufferedReader br = new BufferedReader(new InputStreamReader(is));
//                String regexp = null;
//                while ((regexp = br.readLine()) != null) {
//                    try {
//                        Pattern pattern = Pattern.compile(regexp);
//                        Matcher match = pattern.matcher(str);
//                        if (match.matches()) {
//                            return true;
//                        }
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }
//                if (br != null)
//                    br.close();
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (Exception ex) {
//                }
//            }
//        }
        return false;
    }

    /**
     * 获取CPU序列号
     *
     * @return CPU序列号(16位) 读取失败为null
     */
    private static String getCPUSerial() {
        String line = "";
        String cpuAddress = null;
        try {
            // 读取CPU信息
            Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            // 查找CPU序列号
            for (int i = 1; i < 100; i++) {
                line = input.readLine();
                if (line != null) {
                    // 查找到序列号所在行
                    line = line.toLowerCase();
                    int p1 = line.indexOf("serial");
                    int p2 = line.indexOf(":");
                    if (p1 > -1 && p2 > 0) {
                        // 提取序列号
                        cpuAddress = line.substring(p2 + 1);
                        // 去空格
                        cpuAddress = cpuAddress.trim();
                        break;
                    }
                } else {
                    // 文件结尾
                    break;
                }
            }
            ir.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return cpuAddress;
    }

    /*
    * 获取机器Serial号，Android2.3版本以上有效
    */
    private static String getSerial() {
        String serial = null;
        try {
            if (Build.VERSION.SDK_INT >= 9) {
                Class<Build> clazz = Build.class;
                Field field = clazz.getField("SERIAL");
                serial = (String) field.get(null);
                if (serial != null) {
                    serial = serial.toLowerCase();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return serial;
    }

    /**
     * 获取ANDROID_ID号，Android2.2版本以上系统有效
     *
     * @return
     */
    private static String getAndroidId(Context context) {
        String android_id = null;
        try {
            if (Build.VERSION.SDK_INT >= 8) {
                android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                if (android_id != null) {
                    android_id = android_id.toLowerCase();
                }
            }
        } catch (Throwable ex) {
        }
        return android_id;
    }

    /**
     * 获取网卡的MAC地址
     *
     * @param context
     * @return
     */
    private static String getMacAddress(Context context) {
        String macAddress = null;
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            if (info != null) {
                macAddress = info.getMacAddress();
                if (macAddress != null) {
                    macAddress = macAddress.replaceAll("-", "").replaceAll(":", "").toLowerCase();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return macAddress;
    }

    /**
     * 获取UUID
     *
     * @return
     */
    private static String getUUID() {
        String id = null;
        try {
            id = UUID.randomUUID().toString();
            id = id.replaceAll("-", "").replace(":", "").toLowerCase();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }
}
