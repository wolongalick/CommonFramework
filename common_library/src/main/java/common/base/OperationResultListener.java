package common.base;

import common.bean.ErrorMsg;

public interface OperationResultListener {
    void onSuccess(Object...objs);

    void onFail(ErrorMsg errorMsg);
}