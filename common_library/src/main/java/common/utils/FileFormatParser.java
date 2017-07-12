package common.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * 文件格式解析器
 * Created by cxw on 2016/8/24.
 */
public class FileFormatParser {
    public static final HashMap<String, String> mFileTypes = new HashMap<String, String>();
    static {
        //images
        mFileTypes.put("FFD8FF", "jpg");
        mFileTypes.put("89504E", "png");
        mFileTypes.put("474946", "gif");
        mFileTypes.put("424DD6", "bmp");
    }

    public static String getFileType(String filePath) {
        String fileHeader = getFileHeader(filePath);
        return mFileTypes.get(fileHeader);
    }
    //获取文件头信息
    private static String getFileHeader(String filePath) {
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[3];
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        } finally {
            if(null != is) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }
        return value;
    }
    private static String bytesToHexString(byte[] src){
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();


    }
}
