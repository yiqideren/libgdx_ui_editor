package xyz.white.editor.actors;

import com.kotcrab.vis.ui.widget.VisTextField;

/**
 * 设置输入框只能输入数字和.
 * Created by 10037 on 2017/4/23 0023.
 */
public class DigitsFieldFilter implements VisTextField.TextFieldFilter {

    public static DigitsFieldFilter instance = new DigitsFieldFilter();

    @Override
    public boolean acceptChar(VisTextField textField, char c) {
        return Character.isDigit(c) || c == '.';
    }
}
