package common.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;


public class ProgressDialogUtils {
	private static ProgressDialog mProgressDialog;
	
	public static void showProgressDialog(Activity activity) {
		showProgressDialog(activity,"请稍候...",true,null);
	}
	public static void showProgressDialog(Activity activity,String tipStr,boolean cancelAble){
		showProgressDialog(activity,tipStr, cancelAble,null);
	}
	public static void showProgressDialog(Activity activity,boolean cancelAble) {
		showProgressDialog(activity,"请稍候...", cancelAble,null);
	}
	
	public static void showProgressDialog(Activity activity,CharSequence message) {
		showProgressDialog(activity,message, true,null);
	}

	public static void showProgressDialog(Activity activity,CallBack callBack) {
		showProgressDialog(activity,"请稍候...", true,callBack);
	}

	//============
	public static void showProgressDialog(Activity activity,boolean cancelAble,CharSequence message) {
		showProgressDialog(activity,message, cancelAble,null);
	}

	public static void showProgressDialog(Activity activity,boolean cancelAble,CallBack callBack) {
		showProgressDialog(activity,"请稍候...", cancelAble,callBack);
	}

	//============

	public static void showProgressDialog(Activity activity,CharSequence message,CallBack callBack) {
		showProgressDialog(activity,message, true,callBack);
	}

	public static void showProgressDialog(Activity activity,CharSequence message,boolean cancelable, final CallBack callBack) {
		if(activity==null){
			return;
		}
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(activity,"",message,false,cancelable);
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
			try {
				mProgressDialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mProgressDialog=null;
		}
	}

	public interface CallBack{
		void onDismiss(ProgressDialog progressDialog);
	}
}
