package common.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 价格输入框的工具类,主要用来检查输入的正确性
 * Created by cxw on 2015/12/30.
 */
public class PriceEditTextUtils {
    public static class PriceEditTextChangedListener implements TextWatcher{
        private Context context;
        private EditText editText;
        private String beforeText;
        private int cursorIndex;

        private static final int MAX_NUMBER=9;//最大9位数

        public PriceEditTextChangedListener(Context context,EditText editText) {
            this.context=context;
            this.editText=editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeText=s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String str=s.toString();

            //如果是.开头,则前面补0
            if(str.startsWith(".")){
                editText.setText("0"+str);
                EditTextUtils.moveCursorToLast(editText);
                return;
            }

            if(str.length()==1){
                return;
            }

            //首字符为0时,如果第二位不是点,则将首字符0去除
            if(str.startsWith("0")){
                if(!".".equals(String.valueOf(str.charAt(1)))){
                    editText.setText(str.substring(1,str.length()));
                    EditTextUtils.moveCursorToLast(editText);
                    return;
                }
            }

            //小数点后只能两位
            if(str.contains(".")){
                if(str.length()-1-str.indexOf(".")>2){
                    editText.setText(beforeText);
                    EditTextUtils.moveCursorToLast(editText);
                    return;
                }
            }

            //输入价格小于999999999元
            String[] strings = str.split("\\.");
            if(strings[0].length()>MAX_NUMBER){
                editText.setText(beforeText);
                EditTextUtils.moveCursorToLast(editText);
                return;
            }
        }
    }



}
