package common.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import common.ui.Topbar;


@IntDef({Topbar.TOPBAR_TYPE_NONE, Topbar.TOPBAR_TYPE_IMG, Topbar.TOPBAR_TYPE_TEXT})
@Retention(RetentionPolicy.SOURCE)
public @interface RightType {

}

