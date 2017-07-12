package common.utils;

import java.security.MessageDigest;

public class MD5Utils {

	private MD5Utils() {
	}

	public final static String getMessageDigest(byte[] buffer) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(buffer);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	public final static String stringToMd5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 方法库-加密解密函数
	 * 
	 * @param str  $string 加密的字符串
	 * @param key  $key 加密的密钥
	 * @param type $type 加密的方法-ENCODE|加密 DECODE|解密
	 * @return string
	 */
	public final static String str_code(String str, String key, String type) {

		str = (type.equals("DECODE")) ? Base64.DeCode(str) : str;

		String code = "";
		int key_len = key.length();
		key = stringToMd5(key);
		char[] keych = key.toCharArray();

		char[] ch = str.toCharArray();
		int str_len = ch.length;

		for (int i = 0; i < str_len; i++) {
			int j = (i * key_len) % 32;

			int k = ch[i] ^ keych[j];

			code = code + Character.valueOf((char) k);
		}

		return (type.equals("ENCODE")) ? Base64.EnCode(code) : code;
	}
}
