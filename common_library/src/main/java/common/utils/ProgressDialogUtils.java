package common.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;


public class ProgressDialogUtils {
	private static ProgressDialog mProgressDialog;
	
	public static void showProgressDialog(Context context) {
		showProgressDialog(context,"请稍候...",true,null);
	}
	public static void showProgressDialog(Context context,String tipStr,boolean cancelAble){
		showProgressDialog(context,tipStr, cancelAble,null);
	}
	public static void showProgressDialog(Context context,boolean cancelAble) {
		showProgressDialog(context,"请稍候...", cancelAble,null);
	}
	
	public static void showProgressDialog(Context context,CharSequence message) {
		showProgressDialog(context,message, true,null);
	}

	public static void showProgressDialog(Context context,CallBack callBack) {
		showProgressDialog(context,"请稍候...", true,callBack);
	}

	//============
	public static void showProgressDialog(Context context,boolean cancelAble,CharSequence message) {
		showProgressDialog(context,message, cancelAble,null);
	}

	public static void showProgressDialog(Context context,boolean cancelAble,CallBack callBack) {
		showProgressDialog(context,"请稍候...", cancelAble,callBack);
	}

	//============

	public static void showProgressDialog(Context context,CharSequence message,CallBack callBack) {
		showProgressDialog(context,message, true,callBack);
	}

	public static void showProgressDialog(Context context,CharSequence message,boolean cancelable, final CallBack callBack) {
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(context,"",message,false,cancelable);
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					if(callBack!=null){
						callBack.onDismiss(mProgressDialog);
					}
					mProgressDialog=null;
				}
			});
		} else {
			mProgressDialog.show();
		}
	}
	
	/**
	 * 关闭ProgressDialog
	 */
	public static void dismissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog=null;
		}
	}

	public interface CallBack{
		void onDismiss(ProgressDialog progressDialog);
	}
}
