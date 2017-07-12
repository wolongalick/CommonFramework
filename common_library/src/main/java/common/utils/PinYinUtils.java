package common.utils;

import android.text.TextUtils;
import android.util.Log;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.regex.Pattern;

public class PinYinUtils {
	private  static String regEx = "[\\u4e00-\\u9fa5]+";
	private static Pattern p = Pattern.compile(regEx);
	private static final String TAG=PinYinUtils.class.getSimpleName();
	
	private static HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
	/**
	 * 获得首字母(只得到一个字母)
	 * @param chinese
	 * @return
	 * @since 2015-4-14下午2:09:14
	 * @author cuixingwang
	 */
	public static String getFirstInitial(String chinese){
		return getFirstSpell(chinese.substring(0,1));
	}
	
	/**
	 * 获得首字母(只得到一个字母)
	 * @param chinese
	 * @param isCapital			是否大写
	 * @return
	 * @since 2015-4-14下午2:09:23
	 * @author cuixingwang
	 */
	public static String getFirstInitial(String chinese,boolean isCapital){
		if(TextUtils.isEmpty(chinese)||!chinese.matches(regEx)){
			return "#";
		}
		return getFirstSpell(chinese.substring(0,1),isCapital);
	}
	
	/**
	 * 
	 * 获得首拼字母(例如cxw)
	 * @param chinese				汉字串
	 * @return 						首拼字母,默认为小写
	 * @since 2015-4-14下午1:58:28
	 * @author cuixingwang
	 */
	public static String getFirstSpell(String chinese){
		if(TextUtils.isEmpty(chinese)||(!p.matcher(chinese.substring(0,1)).matches()&&(chinese.charAt(0)>'z')||chinese.charAt(0)<'A')){
			return "#";
		}
		return getFirstSpell(chinese,false);
	}
	
	/**
	 * 
	 * 获得首拼字母(例如CXW)
	 * @param chinese				汉字串(也可以是字母)
	 * @param isCapital				是否大写字母
	 * @return 						汉语拼音首字母
	 * @since 2015-4-14下午1:59:11
	 * @author cuixingwang
	 */
	public static String getFirstSpell(String chinese,boolean isCapital) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		defaultFormat.setCaseType(isCapital ? HanyuPinyinCaseType.UPPERCASE : HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i],
							defaultFormat);
					if (_t != null) {
						pybf.append(_t[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		String str=pybf.toString().replaceAll("\\W", "").trim();
		if(isCapital){
			str=str.toUpperCase();
		}
		return str;
	}
	
	/**
	 * 汉字转成全拼字母(例如cuixingwang)
	 * @param chinese				汉字字符串(也可以是字母)
	 * @return						全拼字母,默认为小写
	 * @since 2015-4-14下午1:46:07
	 * @author cuixingwang
	 */
	public static String getAllSpell(String chinese) {
		return getAllSpell(chinese, false);
	}
	
	/**
	 * 获得全拼字母(例如CUIXINGWANG)
	 * @param chinese				汉字字符串(也可以是字母)
	 * @param isCapital				返回的拼音是否大写,默认为false,代表小写
	 * @return						字母全拼
	 * @since 2015-4-14下午1:46:07
	 * @author cuixingwang
	 */
	public static String getAllSpell(String chinese,boolean isCapital) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		
		defaultFormat.setCaseType(isCapital ? HanyuPinyinCaseType.UPPERCASE : HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i],defaultFormat)[0]);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				} catch(Exception e){
					Log.w(TAG,chinese);
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		String str=pybf.toString();
		if(isCapital){
			str=str.toUpperCase();
		}
		return str; 
	}
}
