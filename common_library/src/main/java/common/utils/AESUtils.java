package common.utils;

import android.text.TextUtils;


import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AESUtils {
    //    public final static String ENCRYPT_TOKEN = "qo9mlfjvo0r4kgnn"; // 替换成和客户端一样的16位长度
//    public final static String IV_KEY = "qo9mlfjvo0r4kgnn"; // 替换成和客户端一样的16位长度
    public static byte[] key = {0xE, 0xB, 0xF, 0xA, 0xE, 0xF, 0xC, 0xB, 0xB, 0xD, 0xD, 0xF, 0xA, 0xA, 0xC, 0xB};
    public static byte[] iv = {0xE, 0xB, 0xF, 0xA, 0xE, 0xF, 0xC, 0xB, 0xB, 0xD, 0xD, 0xF, 0xA, 0xA, 0xC, 0xB};
//    public static byte[] key = null;
//    public static byte[] iv = null;

//    static {
//        try {
//            key = ENCRYPT_TOKEN.getBytes("UTF-8");
//            iv = IV_KEY.getBytes("UTF-8");
//        } catch (Exception e) {
//            System.out.println("dont't init the AES KEY");
//        }
//    }

    /*
     * 将字符串AES加密 加密顺序如下： 先AES加密 然后在base64编码 然后去掉base64编码中的空格。
     */
    public static String AESEncrypt(String src) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(AESUtils.key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(AESUtils.iv));
            byte[] encrypted = cipher.doFinal(src.getBytes("UTF-8"));
            String srcEncode = Base64.encode(encrypted);
            srcEncode = srcEncode.replaceAll("\\s", "");
            srcEncode = URLEncoder.encode(srcEncode, "UTF-8");
            return srcEncode;
        } catch (Exception e) {
            return "";
        }
    }

    public static String enAESEncrypt(String src) {
        if (TextUtils.isEmpty(src)) {
            return "";
        } else {
            return AESEncrypt(src);
        }
    }

    /*
     * 将字符串AES解密 解密顺序如下： 先AES解密 转换成Strng 返回
     */
    public static String AESDecrypt(String src) {
        try {
            String srcDe = URLDecoder.decode(src, "UTF-8");
            byte[] srcDecode = Base64.decode(src);
            SecretKeySpec skeySpec = new SecretKeySpec(AESUtils.key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(
                    AESUtils.iv));
            byte[] dencrypted = cipher.doFinal(srcDecode);
            return new String(dencrypted, "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }
}