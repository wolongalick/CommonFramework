package common.ui.adapter;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by 崔兴旺 on 2016/7/1.
 */
public abstract class SimpleTextWatcherAdapter implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }



    public abstract void afterTextChanged(Editable s);
}
