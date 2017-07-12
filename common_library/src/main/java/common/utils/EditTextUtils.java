package common.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import common.ui.adapter.SimpleTextWatcherAdapter;


/**
 * Created by cxw on 2016/4/15.
 */
public class EditTextUtils {
    /**
     * 将光标移动到末尾
     */
    public static void moveCursorToLast(TextView textView){
        if(textView instanceof EditText){
            ((EditText)textView).setSelection(textView.getText().length());
        }
    }

    /**
     * 限制文本内容长度,超出时,光标移动到末尾
     * @param textView
     */
    public static void limitCount(final TextView textView, final int limit){
        textView.addTextChangedListener(new SimpleTextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String str = textView.getText().toString();
                if(str.length()>limit){
                    str=str.substring(0,limit);

                    // TODO: 2016/11/25
                    // 如果内容是表情,还需要用SmileUtils类转换一下

                    textView.setText(str);

                    //如果是输入框,则将光标移动到末尾
                    moveCursorToLast(textView);
                }
            }
        });
    }


    public static void setTextComplatedListener(final IOnTextComplatedListener iOnTextComplatedListener, final TextView...textViews){
        for (TextView textView:textViews) {
            textView.addTextChangedListener(new SimpleTextWatcherAdapter() {
                @Override
                public void afterTextChanged(Editable s) {
                    for (TextView textView:textViews) {
                        if(TextUtils.isEmpty(textView.getText().toString().trim())){
                            iOnTextComplatedListener.onTextChanged(false);
                            return;
                        }
                    }
                    iOnTextComplatedListener.onTextChanged(true);
                }
            });
        }
    }

    public interface IOnTextComplatedListener{
        void onTextChanged(boolean isCompated);
    }
}
