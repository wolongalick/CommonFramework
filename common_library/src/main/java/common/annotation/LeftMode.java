package common.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import common.ui.Topbar;


@IntDef({Topbar.TOPBAR_LEFT_MODE_BACK, Topbar.TOPBAR_LEFT_MODE_MORE})
@Retention(RetentionPolicy.SOURCE)
public @interface LeftMode {

}