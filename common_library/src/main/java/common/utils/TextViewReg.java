package common.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * Created by cxw on 2017/2/16.
 */

public class TextViewReg {
    private String beforeText;

    /**
     * 调整小数
     * @param textView
     * @param maxDigit      整数位最多位数
     * @param limit         小数位最多位数
     */
    public void adjustDecimal(final TextView textView, final int maxDigit, final int limit){

        checkStr(textView,maxDigit,limit);
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText=s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(textView.getText().toString())){
                    return;
                }
                if(!checkStr(textView,maxDigit,limit)){
                    textView.setText(beforeText);
                    EditTextUtils.moveCursorToLast(textView);
                }
            }
        });
    }

    private boolean checkStr(final TextView textView,final int maxDigit,final int limit){
        return textView.getText().toString().matches("^\\-?([1-9]\\d{0,"+(maxDigit-1)+"}|0)(\\.\\d{0,"+limit+"})?$");
    }
}
