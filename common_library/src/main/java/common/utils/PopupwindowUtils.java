package common.utils;

import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by cxw on 2017/2/27.
 */

public class PopupwindowUtils {

    public static void showCompatible(PopupWindow popupWindow, View parent, View asDropDownView) {
//        if (Build.VERSION.SDK_INT >= 24) {
        // 适配 android 7.0
        int[] location = new int[2];
        asDropDownView.getLocationOnScreen(location);
        popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, location[1] + asDropDownView.getHeight());
//        } else {
//            popupWindow.showAsDropDown(asDropDownView);
//        }
    }

}
