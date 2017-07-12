package common.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by cxw on 2017/3/5.
 */

public class UriUtils {
    /**
     * 获取文件的uri,兼容android N
     * @param context
     * @param filePath
     * @return
     */
    public static Uri getUriCompatibleN(Context context, String filePath){
        return getUriCompatibleN(context,new File(filePath));
    }

    public static Uri getUriCompatibleN(Context context, File file){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){//返回值形如:content://com.acewill.crmoa.demo.fileprovider/external_storage_root/Android/data/com.acewill.crmoa.demo/cache/send/image/a15ac45e-212d-4b61-8280-b5a168c5917f.jpg
            return FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider",file);
        }else {
            return Uri.fromFile(file);//nexus5x中返回值:file:///storage/emulated/0/Android/data/com.acewill.crmoa.demo/cache/send/image/6f7016a1-5791-4666-8b6d-4023e75de0ee.jpg
        }
    }
}
