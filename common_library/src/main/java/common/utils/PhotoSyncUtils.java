package common.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2017/11/17
 * 备注:
 */
public class PhotoSyncUtils {
    private final String TAG = "PhotoSyncUtils";
    private MediaScannerConnection sMediaScannerConnection;

    public interface IScanListener{
        void onScanCompleted(String path,Uri uri);
    }

    public void sync(final Context context, final String filePath) {
        sync(context,filePath,null);
    }


    public void sync(final Context context, final String filePath, final IScanListener iScanListener) {
        sMediaScannerConnection = new MediaScannerConnection(context, new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onMediaScannerConnected() {
                BLog.i(TAG, "scannerConnected, scan local path:" + filePath);
                sMediaScannerConnection.scanFile(filePath, "image/*");
                BitmapUtils.scanFileAsync(context,filePath);

            }

            @Override
            public void onScanCompleted(String path, Uri uri) {
                BLog.i(TAG, "scan complete");
                sMediaScannerConnection.disconnect();
                if(iScanListener!=null){
                    iScanListener.onScanCompleted(path,uri);
                }
            }
        });
        sMediaScannerConnection.connect();
    }


}
