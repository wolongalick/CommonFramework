package common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static common.utils.CommonUtils.parseList2String;

/**
 * 公共的工具类
 */
public class CommonUtils {

    private CommonUtils() {
    }

	/**
     * 检查电话号码的格式
     *
     * @param phoneNum
     * @return
     */
    public static boolean isPhoneNum(String phoneNum) {
        Pattern p = Pattern
                .compile("^((1[0-9])|(1[0-9])|(1[0-9])|(1[0-9]))\\d{9}$");
        Matcher m = p.matcher(phoneNum);
        return m.matches();
    }

    /**
     * 检查邮箱格式
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 检查邮政编码格式
     *
     * @param postcode
     * @return
     */
    public static boolean isPostcode(String postcode) {
        String format = "\\p{Digit}{6}";
		return postcode.matches(format);
    }

	public static boolean isEmptyEditText(EditText editText){
		return TextUtils.isEmpty(editText.getText().toString().trim());
	}
    
    /**
	 * 隐藏手机尾号
	 * @param phoneNum
	 * @return
	 * @since 2015-8-9上午9:45:54
	 * @author cuixingwang
	 */
	public static String hiddenMobileTail(String phoneNum){
		if(phoneNum!=null && phoneNum.length()>4){
			return phoneNum.subSequence(0, phoneNum.length()-4)+"****";
		}else{
			return phoneNum;
		}
	}
	
	/**
	 * 将以逗号分割的String转为List
	 * @param string
	 * @return
	 * @since 2015-8-10下午5:23:10
	 * @author cuixingwang
	 */
	public static List<String> parseString2List(String string){
		List<String> list=new ArrayList<>();
		if(TextUtils.isEmpty(string)){
			return list;
		}else{
			String[] array=string.split(",");
			for (int i = 0; i < array.length; i++) {
				list.add(array[i]);
			}
			return list;
		}
	}
	
	/**
	 * 将list转换为以逗号分割的字符串
	 * @param list
	 * @return 形如:a,b,c,d
	 * @since 2015-8-10下午5:24:58
	 * @author cuixingwang
	 */
	public static <W> String parseList2String(List<W> list){
		return parseList2String("",list);
	}

    /**
     * 将list转换为以逗号分割的字符串
     * @param wrapStr   妹子字符串外面包裹的字符串
     * @param list
     * @param <W>
     * @return  形如:'a','b','c','d'
     */
	public static <W> String parseList2String(String wrapStr,List<W> list){
		if(list!=null && list.size()>0){
			StringBuilder sb=new StringBuilder();
			int count=list.size();
			for (int i = 0; i < count; i++) {
				sb.append(wrapStr+list.get(i)+wrapStr).append(",");
			}
			return sb.toString().substring(0, sb.toString().length()-1);
		}else{
			return "";
		}
	}


	/**
	 * 将list转换为以separator的字符串
	 * @param list
	 * @param separator
     * @return
     */
	public static <W> String parseList2String(List<W> list,String separator){
		if(list!=null && list.size()>0){
			StringBuilder sb=new StringBuilder();
			int count=list.size();
			for (int i = 0; i < count; i++) {
				W str = list.get(i);
				if(str!=null && !"".equals(str)){
					sb.append(str).append(separator);
				}
			}
			String string = sb.toString();
			if(string.length()==0){
				return "";
			}
			return string.substring(0, string.length()-1);
		}else{
			return "";
		}
	}

	public static String[] parseList2Array(List<String> list){
		if(list==null || list.isEmpty()){
			return new String[]{};
		}
		int count=list.size();
		String[] strings=new String[count];
		for (int i = 0; i < count; i++) {
			strings[i]=list.get(i);
		}
		return strings;
	}

	/**
	 * 将list中model的某个属性取出来,用逗号分割,拼接成字符串
	 * @param list
	 * @param fieldName
	 * @param <W>
	 * @return
	 */
	public static  <W> String buildStringsFromList(List<W> list,String fieldName){
		int count = list.size();
		StringBuilder sb=new StringBuilder();
		String methodName="get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
		for (int i = 0; i < count; i++) {
			W w = list.get(i);
			try {
				String value = (String) w.getClass().getMethod(methodName).invoke(w);
				if(TextUtils.isEmpty(value)){
					continue;
				}
				sb.append(value).append(",");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		if(TextUtils.isEmpty(sb)){
			return "";
		}
		return sb.toString().substring(0, sb.toString().length()-1);
	}

	/**
	 * 将model集合转换成其中的某个属性集合
	 * @param list
	 * @param fieldName
	 * @param <W>
	 * @param <Q>
	 * @return
	 */
	public static <W, Q> List<Q> parseModels2Fields(List<W> list,String fieldName) {
		int count = list.size();
		List<Q> qlist = new ArrayList<>();
		String methodName = "get" + fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
		for (int i = 0; i < count; i++) {
			W w = list.get(i);
			try {
				Method method = w.getClass().getMethod(methodName);
				Q qValue = (Q) method.invoke(w);
				qlist.add(qValue);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return qlist;
	}



	/**
	 * 手机号加"*"*/
	public static String changePhone(String str){
		String phone="";
		if (str.length()==11) {
			phone = str.substring(0,3)+"******"+str.substring(9,11);
		}
		return phone;
	}

	public static boolean isNumber(String str){
		return isInteger(str) || isDecimal(str);
	}

	/**
	 * 判断该字符串是否为整数
	 * @param str
	 * @return
	 * @since 2015-8-29上午1:44:56
	 * @author cuixingwang
	 */
	public static boolean isInteger(String str) {
		if(TextUtils.isEmpty(str)){
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 将字符串保留N位小数
	 * @param str
	 * @param precision
     * @return
     */
	public static String round(String str, int precision){
		String reslut = null;
		try {
			if(str==null || "".equals(str)){
                return "";
            }
			StringBuilder sb=new StringBuilder();
			sb.append("#");
			if(precision>0){
                sb.append(".");
            }
			for (int i = 0; i < precision; i++) {
                sb.append("0");
            }

			String format=sb.toString();
			DecimalFormat df = new DecimalFormat(format);

			reslut = df.format(Double.parseDouble(str));

			int indexOf = reslut.indexOf(".");

			if(indexOf!=-1){
                String prefix=reslut.substring(0,reslut.indexOf("."));

                if(!isNumber(prefix)){

                    reslut=reslut.substring(0,indexOf)+"0"+reslut.substring(indexOf,reslut.length());
                }
            }
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "";
		}

		return reslut;
	}

	/**
	 * 是否为小数
	 * @param orginal
	 * @return
     */
	public static boolean isDecimal(String orginal) {
		return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);
	}

	private static boolean isMatch(String regex, String orginal) {
		if (orginal == null || orginal.trim().equals("")) {
			return false;
		}

		Pattern pattern = Pattern.compile(regex);
		Matcher isNum = pattern.matcher(orginal);
		return isNum.matches();
	}


	/**
	 * 检测Sdcard是否存在
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * JsonStr字符串BOM头处理
	 * 
	 * @param data
	 * @return
	 */
	public static String getNoBOMStr(String data) {
		if (data != null && data.startsWith("\ufeff")) {
			return data.substring(1);
		}
		return data;
	}

	public static String removeBOM(String data) {

		if (TextUtils.isEmpty(data)) {
			return data;
		}

		if (data.startsWith("\ufeff")) {
			// Log.e(TAG, "Json字符串BOM头处理");
			return data.substring(1);
		} else {
			return data;
		}
	}

	public static boolean isAvilible(Context context, String packageName) {
		final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		List<String> pName = new ArrayList<String>();// 用于存储所有已安装程序的包名
		// 从pinfo中将包名字逐一取出，压入pName list中
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				pName.add(pn);
			}
		}
		return pName.contains(packageName);// 判断pName中是否有目标程序的包名，有TRUE，没有FALSE
	}

	public static String processNullStr(Object originalStr){
		return processNullStr(originalStr, "暂无");
	}

	/**
	 * 处理空字符串
	 * @param originalStr
	 * @param defaultStr
	 * @return
	 */
	public static String processNullStr(Object originalStr,String defaultStr){
		if(originalStr==null || "".equals(originalStr.toString()) || "null".equalsIgnoreCase(originalStr.toString())){
			return defaultStr;
		}
		try {
			if(Double.parseDouble(originalStr.toString())!=0){
				return originalStr+"";
			}
		} catch (NumberFormatException e) {
			return originalStr.toString();
		}
		return defaultStr;

	}

	/**
	 * 拷贝
	 * @param fromObj
	 * @param toObj
	 * @return
	 */
	public static boolean copy(Object fromObj, Object toObj) {
		Field[] fromObjFields = fromObj.getClass().getDeclaredFields();
		Field[] toObjFields = toObj.getClass().getDeclaredFields();

		int fromObjMethodsCount = fromObjFields.length;
		int toObjMethodsCount = toObjFields.length;

		if (fromObjMethodsCount != toObjMethodsCount) {
			return false;
		}
		for (int i = 0; i < fromObjMethodsCount; i++) {
			Field field = fromObjFields[i];
			Class<?> type = field.getType();
			//属性名
			String fieldName=field.getName();
			String upperFieldName=fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
			//根据属性名得到对应的方法名
			String methodNameGet="get"+upperFieldName;
			if(type==boolean.class){
				methodNameGet="is"+upperFieldName;
			}
			String methodNameSet="set"+upperFieldName;
			//调用方法
			Method methodGet = null;
			try {
				methodGet = fromObj.getClass().getMethod(methodNameGet);
				Object valueGet=methodGet.invoke(fromObj);
				Method methodSet = toObj.getClass().getMethod(methodNameSet, type);
				methodSet.invoke(toObj,valueGet);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 获得百分比
	 * @param aDouble
	 * @return
	 */
	public static String getPercent(Double aDouble){
		DecimalFormat decimalFormat=new DecimalFormat("0.00%");
		decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
		return decimalFormat.format(aDouble);
	}

	/**
	 * 获得粗略的double
	 * @param aDouble
	 * @return
	 */
	public static double getRoughlyDouble(Double aDouble){
		DecimalFormat df=new DecimalFormat("0.00000");
		return Double.parseDouble(df.format(aDouble));
	}




	/**
	 * 将秒数转换成分秒
	 * @param second
	 * @return
	 */
	public static String parseDuration(int second){
		if(second<=60){
			return second+"\"";
		}else{
			int modulo=second%60;
			if(modulo==0){
				return second/60+"'";
			}else{
				return second/60+"'"+modulo+"\"";
			}
		}
	}


	/**
	 * String数组转为String字符串
	 * @param strings
	 * @return
	 */
	public static String array2String(String[] strings){
		StringBuilder sb=new StringBuilder();
		for (String str:strings) {
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * 获得该字符串中小数点后面有几位
	 * @param str
	 * @return
	 */
	public static int getPrecision(String str){
		int index = str.indexOf(".");
		if(index==-1){
			return 0;
		}else{
			return str.length()-index-1;
		}
	}


}
