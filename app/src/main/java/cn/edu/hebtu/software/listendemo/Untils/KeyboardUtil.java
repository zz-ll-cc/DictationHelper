package cn.edu.hebtu.software.listendemo.Untils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import java.lang.reflect.Method;

import cn.edu.hebtu.software.listendemo.R;

public class KeyboardUtil {
    private KeyboardView keyboardView;
    private Keyboard k1;// 字母键盘
    private boolean isUpper = false;// 是否大写

    private static final int LEFT_CODE = -11;//中文横线
    private static final int RIGHT_CODE = -12;//中文横线
    private EditText editText;

    public KeyboardUtil(KeyboardView keyboardView, EditText editText) {
        editText.setInputType(InputType.TYPE_NULL); //setInputType为InputType.TYPE_NULL   不然会弹出系统键盘

        k1 = new Keyboard(editText.getContext(), R.xml.letter);
        this.keyboardView = keyboardView;
        this.editText = editText;
        this.keyboardView.setKeyboard(k1);
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);
        this.keyboardView.setOnKeyboardActionListener(onKeyboardActionListener);
    }

    OnKeyboardActionListener onKeyboardActionListener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes ){
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();
            switch (primaryCode) {
                case Keyboard.KEYCODE_DELETE://回退
                    if (editable != null && editable.length() > 0) {
                        if (start > 0) {
                            editable.delete(start - 1, start);
                        }
                    }
                    break;
                case Keyboard.KEYCODE_CANCEL://完成
                    hideKeyboard();
                    break;
                case LEFT_CODE: //向左
                    if (start > 0) {
                        editText.setSelection(start - 1);
                    }
                    break;
                case RIGHT_CODE:// 向右
                    if (start < editText.length()) {
                        editText.setSelection(start + 1);
                    }
                    break;
                default:
                    String str = Character.toString((char) primaryCode);
                    if (isWord(str)) {
                        if (isUpper) {
                            str = str.toUpperCase();
                        } else {
                            str = str.toLowerCase();
                        }
                    }
                    editable.insert(start, str);
                    break;
            }
        }
    };

    // Activity中获取焦点时调用，显示出键盘
    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    // 隐藏键盘
    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.GONE);
        }
    }

    private boolean isWord(String str) {
        return str.matches("[a-zA-Z]");
    }

}

