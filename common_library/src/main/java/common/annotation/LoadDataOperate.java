package common.annotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import common.utils.CommonConstant;

/**
 * 功能:
 * 作者: 崔兴旺
 * 日期: 2018/1/22
 * 备注:
 */
@StringDef({CommonConstant.LoadDataOperate.REFRESH, CommonConstant.LoadDataOperate.LOADMORE, CommonConstant.LoadDataOperate.PRE_LOADMORE})
@Retention(RetentionPolicy.SOURCE)
public @interface LoadDataOperate {

}
