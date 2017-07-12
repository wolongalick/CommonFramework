package common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtils {

    private static final String TAG = BitmapUtils.class.getSimpleName();

    public enum CropWay{
        top,
        middle
    }

    public enum ShowLocation{
        top,
        middle
    }

    private BitmapUtils() {
        throw new AssertionError();
    }

    /**
     * convert Bitmap to byte array 把bitmap转成byte数组
     *
     * @param b
     * @return
     */
    public static byte[] bitmapToByte(Bitmap b) {
        if (b == null) {
            return null;
        }

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(CompressFormat.JPEG, 100, o);
        return o.toByteArray();
    }

    /**
     * convert byte array to Bitmap 把byte数组转成bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap byteToBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory
                .decodeByteArray(b, 0, b.length);
    }

    /**
     * convert Drawable to Bitmap 把Drawable转成bitmap
     *
     * @param d
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable d) {
        return d == null ? null : ((BitmapDrawable) d).getBitmap();
    }

    /**
     * convert Bitmap to Drawable 把bitmap转成Drawable
     *
     * @param b
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap b) {
        return b == null ? null : new BitmapDrawable(b);
    }

    /**
     * convert Drawable to byte array 把Drawable转成byte数组
     *
     * @param d
     * @return
     */
    public static byte[] drawableToByte(Drawable d) {
        return bitmapToByte(drawableToBitmap(d));
    }

    /**
     * convert byte array to Drawable 把byte数组转成Drawable
     *
     * @param b
     * @return
     */
    public static Drawable byteToDrawable(byte[] b) {
        return bitmapToDrawable(byteToBitmap(b));
    }

    /**
     * scale image 缩放Bitmap
     *
     * @param org
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
        return scaleImage(org, (float) newWidth / org.getWidth(),
                (float) newHeight / org.getHeight());
    }

    /**
     * scale image 缩放Bitmap
     *
     * @param org
     * @param scaleWidth  sacle of width
     * @param scaleHeight scale of height
     * @return
     */
    public static Bitmap scaleImage(Bitmap org, float scaleWidth, float scaleHeight) {
        if (org == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(),
                matrix, true);
    }

    public static Bitmap adjustPhotoRotation(Bitmap oldBitmap, final int orientationDegree) {
        return adjustPhotoRotation(oldBitmap, orientationDegree, ShowLocation.middle);
    }

    public static Bitmap adjustPhotoRotationWithFrontCamera(Bitmap oldBitmap){
        int oldWidth = oldBitmap.getWidth();//1014
        int oldHeight = oldBitmap.getHeight();//579
        Matrix matrix = new Matrix();
        matrix.setRotate(270,oldHeight/2,oldHeight/2);

        matrix.postTranslate(0, oldWidth-oldHeight);


        Bitmap newBitmap = Bitmap.createBitmap(oldHeight,oldWidth,  Bitmap.Config.RGB_565);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(oldBitmap, matrix, paint);
        return newBitmap;
    }

    public static Bitmap adjustPhotoRotation(Bitmap oldBitmap, int orientationDegree,int x,int y){
        Matrix matrix = new Matrix();
        matrix.setRotate(orientationDegree, x, y);

        Bitmap newBitmap = Bitmap.createBitmap(oldBitmap.getWidth(),oldBitmap.getHeight(),  Bitmap.Config.RGB_565);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(oldBitmap, matrix, paint);
        return newBitmap;
    }




    /**
     * 旋转图片
     * @param oldBitmap
     * @param orientationDegree
     * @return
     */
    public static Bitmap adjustPhotoRotation(Bitmap oldBitmap, final int orientationDegree,ShowLocation showLocation) {
        Matrix matrix = new Matrix();
        matrix.setRotate(orientationDegree, (float) oldBitmap.getWidth() / 2, (float) oldBitmap.getHeight() / 2);

        if(showLocation==ShowLocation.top){
            float targetX, targetY;
            if (orientationDegree == 90) {
                targetX = oldBitmap.getHeight();
                targetY = 0;
            } else {
                targetX = oldBitmap.getHeight();
                targetY = oldBitmap.getWidth();
            }
            final float[] values = new float[9];
            matrix.getValues(values);
            float x1 = values[Matrix.MTRANS_X];
            float y1 = values[Matrix.MTRANS_Y];
            matrix.postTranslate(targetX - x1, targetY - y1);
        }



        Bitmap newBitmap = Bitmap.createBitmap(oldBitmap.getWidth(),oldBitmap.getHeight(),  Bitmap.Config.RGB_565);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(oldBitmap, matrix, paint);
        return newBitmap;
    }

    public static Bitmap adjustPhotoRotation2(Bitmap oldBitmap,final int orientationDegree){
        Matrix matrix = new Matrix();
        matrix.setRotate(orientationDegree);
        int newWidth = oldBitmap.getWidth();
        int newHeight = oldBitmap.getHeight();
        switch (orientationDegree){
            case 90:
                newWidth=oldBitmap.getHeight();
                newHeight=oldBitmap.getWidth();
                matrix.postTranslate(newWidth,0);
                break;
        }

        Bitmap newBitmap = Bitmap.createBitmap(newWidth,newHeight,  Bitmap.Config.RGB_565);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(oldBitmap, matrix, paint);
        return newBitmap;
    }

    public static Bitmap cropImage(Bitmap bitmap,CropWay cropWay) {
        return cropImage(bitmap,cropWay,0,0);
    }

    /**
     * 按正方形裁切图片
     */
    public static Bitmap cropImage(Bitmap bitmap,CropWay cropWay,int offsetX,int offsetY) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX=0;
        int retY=0;
        switch (cropWay){
            case top:
                //从图片左上角开始裁剪
                retX= 0;
                retY= 0;
                break;
            case middle:
                retX= w > h ? (w - h) / 2 : 0;//基于原图，取正方形左上角x坐标
                retY= w > h ? 0 : (h - w) / 2;
                break;
        }
        retX+=offsetX;
        retY+=offsetY;

        //下面这句是关键
        return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
    }

    /**
     * 保存图片到相册
     *
     * @param context
     * @param bmp
     * @author cuixingwang
     * @since 2015-8-19下午4:39:43
     */
    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "bbqnw");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".png";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
            scanFileAsync(context, file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
    }

    /**
     * 通知相册更新显示指定的图片
     *
     * @param context
     * @param filePath
     * @author cuixingwang
     * @since 2015-8-19下午4:41:46
     */
    public static void scanFileAsync(Context context, String filePath) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(UriUtils.getUriCompatibleN(context,filePath));
        context.sendBroadcast(scanIntent);
    }

    /**
     * 向bitmap中心添加logo
     *
     * @param backgroundBitmap
     * @param logoBitmap
     * @return
     * @author cuixingwang
     * @since 2015-8-19下午4:49:21
     */
    public static Bitmap addLogo2Bitmap(Bitmap backgroundBitmap, Bitmap logoBitmap) {
        //二维码和logo合并
        Bitmap bitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(), backgroundBitmap.getHeight(), backgroundBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        //二维码
        canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        //logo绘制在二维码中央
        canvas.drawBitmap(logoBitmap, backgroundBitmap.getWidth() / 2 - logoBitmap.getWidth() / 2, backgroundBitmap.getHeight() / 2 - logoBitmap.getHeight() / 2, null);
        return bitmap;
    }

    @SuppressLint("NewApi")
    public static Bitmap getimageFromRes(Context context, int resId) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, newOpts);//此时返回bm为空
        calculateOptions(newOpts);
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeResource(context.getResources(), resId, newOpts);
        return bitmap;//压缩好比例大小后再进行质量压缩
    }

    public static Bitmap getimageFromFile(File file) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;//目的是只得到图片宽高
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), newOpts);//此时返回bm为空
        calculateOptions(newOpts);
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), newOpts);
        return bitmap;//压缩好比例大小后再进行质量压缩
    }

    public static Bitmap getimageFromFile(String filePath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;//目的是只得到图片宽高
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, newOpts);//此时返回bm为空
        calculateOptions(newOpts);
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(filePath, newOpts);
        return bitmap;//压缩好比例大小后再进行质量压缩
    }


    @SuppressLint("NewApi")
    public static Bitmap getimageFromFile(File file,int maxWidth,int maxHeight) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;//目的是只得到图片宽高
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), newOpts);//此时返回bm为空
        calculateOptions(newOpts,maxWidth,maxHeight);
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), newOpts);
        return bitmap;//压缩好比例大小后再进行质量压缩
    }


    public static Bitmap createNewBitmap(File file,int maxWidth,int maxHeight){
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        //现在主流手机比较多是640*480分辨率，所以高和宽我们设置为
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        float rate= 1;//be=1表示不缩放

        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        if(width >= height){
            if(width > maxWidth){
                //缩放宽度
                rate = (float) maxWidth/width;
                Matrix matrix = new Matrix();
                matrix.postScale(rate, rate);
                return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
            }
        }else{
            if(height > maxHeight){
                //缩放高度
                rate = (float)maxHeight/height;
                Matrix matrix = new Matrix();
                matrix.postScale(rate, rate);
                return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
            }
        }
        return bitmap;
    }


    /**
     * 计算Options
     *
     * @param options
     * @author cuixingwang
     * @since 2015-8-24下午11:59:35
     */
    private static void calculateOptions(BitmapFactory.Options options) {
        options.inJustDecodeBounds = false;
        int w = options.outWidth;
        int h = options.outHeight;
        //现在主流手机比较多是1280*720分辨率，所以高和宽我们设置为
        float hh = 320f * 4;//这里设置高度为1280f
        float ww = 240f * 3;//这里设置宽度为720f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int inSampleSize = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            inSampleSize = (int) (options.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            inSampleSize = (int) (options.outHeight / hh);
        }
        if (inSampleSize <= 0)
            inSampleSize = 1;
        options.inSampleSize = inSampleSize;//设置缩放比例
    }
    public static void calculateOptions(BitmapFactory.Options options,int maxWidth,int maxHeight) {
        options.inJustDecodeBounds = false;
        int w = options.outWidth;
        int h = options.outHeight;
        //现在主流手机比较多是1280*720分辨率，所以高和宽我们设置为
        int hh = maxWidth;
        int ww = maxHeight;
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int inSampleSize = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            int remainder = options.outWidth % hh;//余数
            inSampleSize = remainder==0?remainder:remainder+1;
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            int remainder = options.outHeight % hh;//余数
            inSampleSize = remainder==0?remainder:remainder+1;
        }
        if (inSampleSize <= 0)
            inSampleSize = 1;
        options.inSampleSize = inSampleSize;//设置缩放比例
    }

    public static Bitmap createImageThumbnail(String filePath,int newHeight,int newWidth) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int oldHeight = options.outHeight;
        int oldWidth = options.outWidth;
// Log.i(TAG, "高度是：" + oldHeight + "，宽度是：" + oldWidth);
        int ratioHeight = oldHeight / newHeight;
        int ratioWidth = oldWidth / newWidth;

        options.inSampleSize = ratioHeight > ratioWidth ? ratioWidth
                : ratioHeight;

        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
// Log.i(TAG, "高度是：" + options.outHeight + "，宽度是：" + options.outWidth);
        return bm;
    }

    @SuppressLint("NewApi")
    public static Bitmap compressImage(Bitmap image, final int max) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        image.compress(CompressFormat.JPEG, quality, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        while (baos.toByteArray().length / 1024 > max) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(CompressFormat.JPEG, quality, baos);//这里压缩options%，把压缩后的数据存放到baos中
            quality -= 10;//每次都减少10
            if (quality < 10) {
                quality -= 1;//每次都减少1
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        BLog.i(TAG, "按质量压缩后,Width=" + bitmap.getWidth() + ",Height=" + bitmap.getHeight() + ",占内存=" + bitmap.getByteCount() / 1024 + "kb");
        return bitmap;
    }

    /**
     * 压缩图片到指定文件
     *
     * @param image   图片
     * @param max     最大多少kb
     * @param outFile 存放图片的文件
     * @author cuixingwang
     * @since 2015-8-24下午9:55:58
     */
    @SuppressLint("NewApi")
    public static void compressImage(Bitmap image, final int max, File outFile) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        image.compress(CompressFormat.JPEG, quality, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        while (baos.toByteArray().length / 1024 > max) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(CompressFormat.JPEG, quality, baos);//这里压缩options%，把压缩后的数据存放到baos中
            quality -= 5;//每次都减少10
            if (quality < 10) {
                quality -= 1;//每次都减少1
            }
            if(quality<=0){
                quality=1;
            }
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(outFile);
            fileOutputStream.write(baos.toByteArray(), 0, baos.toByteArray().length);
            BLog.i(TAG, "按质量压缩后,占内存=" + baos.toByteArray().length / 1024 + "kb");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 毛玻璃效果
     *
     * @param context
     * @param sentBitmap
     * @param radius
     * @return
     */
    public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        // Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int temp = 256 * divsum;
        int dv[] = new int[temp];
        for (i = 0; i < temp; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        // Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

    /**
     * 截图正方形bitmap
     *
     * @param bitmap
     * @param edgeLength
     * @return
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {


        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg);
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

    /**
     * 根据本地路径获取bitmap
     *
     * @param url
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitmapFromUrl(String url, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 设置了此属性一定要记得将值设置为false
        Bitmap bitmap = BitmapFactory.decodeFile(url, options);
        // 防止OOM发生
        options.inJustDecodeBounds = false;

        int oldHeight = options.outHeight;
        int oldWidth = options.outWidth;
        // Log.i(TAG, "高度是：" + oldHeight + "，宽度是：" + oldWidth);
        int ratioHeight = oldHeight / height;
        int ratioWidth = oldWidth / width;

        options.inSampleSize = ratioHeight > ratioWidth ? ratioWidth
                : ratioHeight;

        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(url, options);
        // Log.i(TAG, "高度是：" + options.outHeight + "，宽度是：" + options.outWidth);
        return bm;


    }

    public static void saveBitmap2File(Bitmap bitmap,String fileName,CompressFormat compressFormat) {
        File file = new File(fileName);
        boolean newFile = FileUtils.createFile(file);
        BLog.i("保存图片结果:"+newFile);

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(compressFormat, 100, fOut);
        try {
            if (fOut != null) {
                fOut.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveBitmap2File(Bitmap bitmap,String fileName) {
        saveBitmap2File(bitmap,fileName,CompressFormat.JPEG);
    }


}
