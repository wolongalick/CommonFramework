package common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gyw
 * @version 1.0
 * @time: 2015-6-11 上午11:19:02
 * @fun: 文件工具类
 */
public class FileUtils {

	public static final String ROOT_DIR = "qingyin";
	public static final String DOWNLOAD_DIR = "download";
	public static final String CACHE_DIR = "cache";
	private static final String TAG = "FileUtil";

	/** 判断SD卡是否挂载 */
	public static boolean isSDCardAvailable() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	/** 获取缓存目录 */
	@Deprecated
	public static String getCacheDir(Context context) {
		return getDir(context, CACHE_DIR);
	}

	/** 获取下载目录 */
	public static String getDownloadDir(Context context) {
		return getDir(context, DOWNLOAD_DIR);
	}

	/** 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录 */
	public static String getDir(Context context, String name) {
		StringBuilder sb = new StringBuilder();
		// boolean isFirstInstallSd = SharedPreUtil.getBooleanSharedPre(context,
		// "first_install_sd", true);
		if (isSDCardAvailable()) {
			sb.append(getExternalStoragePath());
		} else {
			sb.append(getCachePath(context));
		}
		sb.append(name);
		sb.append(File.separator);
		String path = sb.toString();
		if (createDirs(path)) {
			return path;
		} else {
			return null;
		}
	}

	/** 获取SD下的应用目录
	 *
	 */
	public static String getExternalStoragePath() {
		StringBuilder sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
		sb.append(File.separator);
		sb.append(ROOT_DIR);
		sb.append(File.separator);
		return sb.toString();
	}

	/** 获取应用的cache目录 */
	public static String getCachePath(Context context) {
		File f = context.getCacheDir();
		if (null == f) {
			return null;
		} else {
			return f.getAbsolutePath() + "/";
		}
	}

	/** 创建文件夹 */
	public static boolean createDirs(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists() || !file.isDirectory()) {
			return file.mkdirs();
		}
		return true;
	}

	public static boolean createFile(File file) {
		if (!file.exists()) {// 如果文件不存在，或者是文件夹
			String parent = file.getParent();
			File parentFile = new File(parent);// 根据父路径创建文件对象
			if (!parentFile.exists() || !parentFile.isDirectory()) {
				parentFile.mkdirs();
			}
			try {
				file.createNewFile();// 创建文件
			} catch (Exception e) {
				BLog.e("创建文件失败:"+e.getMessage());
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/** 创建文件 */
	public static boolean createFile(String filePath) {
		File file = new File(filePath);
		return createFile(file);
	}

	/** 判断文件是否存在 */
	public static boolean isExistFile(String filePath) {
		if(TextUtils.isEmpty(filePath)){
			return false;
		}
		File file = new File(filePath);
		return file.exists();
	}

	/** 删除文件 */
	public static void deleteFile(Context context, String filePath) {
		File file = new File(filePath);
		if (file.exists()) { // 判断文件是否存在
			file.delete(); // 删除文件
		} else {
			// ToastUtil.showShortToast(context, "文件不存在或已删除");
		}
	}

	/** 删除文件 */
	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) { // 判断文件是否存在
			return file.delete(); // 删除文件
		}
		return false;
	}

	/**
	 * 
	 * @param file
	 */
	public static void deleteFileFolder(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
				return;
			}
			if (file.isDirectory()) {
				File[] childFile = file.listFiles();
				if (childFile == null || childFile.length == 0) {
					file.delete();
					return;
				}
				for (File f : childFile) {
					deleteFileFolder(f);
				}
				file.delete();
			}
		}
	}

	public static boolean renameFile(File srcfile,String newFileName){
		if(srcfile==null || !srcfile.exists()){
			return false;
		}
		return srcfile.renameTo(new File(srcfile.getParentFile(),newFileName));
	}

	private static String IMAGE_PATH = "";
	private static String FILE_NAME= "/icon_app.png";
	private static final int sharePictureResId = 0;
	/**
	 * 创建本地分享图片
	 * @return 
	 * 
	 */
	public static String createLocalPic(Context context){
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			IMAGE_PATH = Environment
					.getExternalStorageDirectory()
					.getAbsolutePath()
					+ FILE_NAME;
		} else {
			IMAGE_PATH = context.getFilesDir()
					.getAbsolutePath() + FILE_NAME;
		}
		File shareAppFile = new File(IMAGE_PATH);
		if (!shareAppFile.exists()) {
			try {
				shareAppFile.createNewFile();
				Bitmap bt = BitmapFactory.decodeResource(
						context.getResources(), sharePictureResId);
				FileOutputStream fos = new FileOutputStream(
						shareAppFile);
				bt.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return IMAGE_PATH;
	}

    public static void bytes2File(byte[] bytes,String filePath){
        bytes2File(bytes,filePath,false);
    }
	public static void bytes2File(byte[] bytes,String filePath,boolean isAppend){
		FileOutputStream out=null;
		try {
			out=new FileOutputStream(filePath,isAppend);
			out.write(bytes,0,bytes.length);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}finally {
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * 将文件转换成byte数组
	 * @param file
	 * @return
	 * @since 2015-8-24下午9:53:49
	 * @author cuixingwang
	 */
	public static byte[] file2Bytes(File file){
		if(file==null || !file.exists()){
			return null;
		}
		byte[] buf=new byte[1024];
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		InputStream in=null;
		try {
			in=new FileInputStream(file);
			int len=0;
			while((len=in.read(buf))!=-1){
				out.write(buf,0,len);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return out.toByteArray();
	}

	/**
	 * 复制文件
	 * @param fromFile
	 * @param toFile
     */
	public static void copyfile(String fromFile, String toFile){
		copyfile(new File(fromFile),new File(toFile));
	}

	public static void cutFile(String fromFile, String toFile){
		cutFile(new File(fromFile),new File(toFile));
	}

	/**
	 * 剪切文件
	 * @param fromFile
	 * @param toFile
     */
	public static void cutFile(File fromFile, File toFile){
		copyfile(fromFile,toFile);
		fromFile.delete();
	}

	/**
	 * 复制文件
	 * 
	 * @param fromFile
	 * @param toFile
	 * @since 2015-9-22下午3:00:15
	 * @author zhanghebin
	 */
	public static boolean copyfile(File fromFile, File toFile) {
		FileInputStream fosfrom = null;
		FileOutputStream fosto = null;
		if (!fromFile.exists()) {
			return false;
		}
		if (!fromFile.isFile()) {
			return false;
		}
		if (!fromFile.canRead()) {
			return false;
		}
        try {
            boolean newFile = createFile(toFile);
            BLog.i("新建文件是否成功:"+newFile);
			if(!newFile){
				return false;
			}
            fosfrom = new FileInputStream(fromFile);
			fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024*3];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c); // 将内容写到新文件当中
			}
			return true;
		} catch (Exception ex) {
			Log.e("readfile", ex.getMessage());
			return false;
		} finally {
			BLog.i("cxw","关闭流");
			try {
				if (fosfrom != null) {
						fosfrom.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (fosto != null) {
					fosto.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获得文件扩展名(不包含.)
	 * @param filePathOrUrl
	 * @return
     */
	public static String getExtName(String filePathOrUrl){
		if(TextUtils.isEmpty(filePathOrUrl) || !filePathOrUrl.contains(".")){
			return "";
		}
		if(filePathOrUrl.lastIndexOf(".") < filePathOrUrl.lastIndexOf("/")){
			return "";
		}

		try {
			return filePathOrUrl.substring(filePathOrUrl.lastIndexOf(".")+1);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获得文件扩展名
	 * @param file
	 * @return
     */
	public static String getExtName(File file){
		return getExtName(file.getAbsolutePath());
	}

	/**
	 * 获得文件名
	 * @param filePathOrUrl
	 * @return
     */
	public static String getFileName(String filePathOrUrl){
		try {
			String fileName=filePathOrUrl.substring(filePathOrUrl.lastIndexOf("/")+1);
			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}



	public static void writeFile(String filePath,String content){
		if(TextUtils.isEmpty(filePath)){
			return;
		}
		writeFile(new File(filePath),content);
	}

	public static void writeFile(File file,String content){
		boolean isExists = file.exists();
		if(!isExists){
			isExists=createFile(file);
		}
		if(isExists){
			FileOutputStream out=null;
			try {
				out=new FileOutputStream(file,true);
				byte[] bytes = content.getBytes();
				out.write(bytes,0,bytes.length);
				out.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(out!=null){
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 删除文件前几行
	 * @param file    	文件
	 * @param lineNum	前几行
	 * @return			删除的内容集合
	 * @throws IOException
	 */
	public static List<String> readAndRemoveFirstLines(File file, int lineNum)throws IOException {
		if(file==null || !file.exists()|| lineNum<=0){
			return null;
		}
		List<String> strList = null;
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "rw");
			// Initial write position
			long writePosition = raf.getFilePointer();
			for (int i = 0; i < lineNum; i++) {
				String line = raf.readLine();
				if (line == null) {
					break;
				}
				//必须转码,否则读取出现乱码
				line = new String(line.getBytes("ISO-8859-1"),"utf-8");
				if(strList==null){
					strList=new ArrayList<String>();
				}
				strList.add(line);
			}
			// Shift the next lines upwards.
			long readPosition = raf.getFilePointer();

			byte[] buff = new byte[1024];
			int n;
			while (-1 != (n = raf.read(buff))) {
				raf.seek(writePosition);
				raf.write(buff, 0, n);
				readPosition += n;
				writePosition += n;
				raf.seek(readPosition);
			}
			raf.setLength(writePosition);
		} catch (IOException e) {
			BLog.e("readAndRemoveFirstLines error");
			throw e;
		} finally {
			try {
				if (raf != null) {
					raf.close();
				}
			} catch (IOException e) {
				BLog.e("close RandomAccessFile error");
				throw e;
			}
		}

		return strList;
	}

}
