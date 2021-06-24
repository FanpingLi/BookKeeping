package com.lifp.bookkeeping.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.lifp.bookkeeping.R;
import com.lifp.bookkeeping.listener.OnKeyBoardListener;

/**
 * 自定义KeyBoard
 *
 * @Author lifp
 * @Date 20-12-10
 */
public class KeyBoardUtils {
    private final Keyboard mKeyBoard;
    private KeyboardView mKeyboardView;
    private EditText mEditText;
    private OnKeyBoardListener mOnKeyBoardListener;

    public KeyBoardUtils(KeyboardView mKeyboardView, final EditText mEditText) {
        this.mKeyboardView = mKeyboardView;
        this.mEditText = mEditText;
        // 取消弹出系统键盘
        this.mEditText.setInputType(InputType.TYPE_NULL);
        mKeyBoard = new Keyboard(this.mEditText.getContext(), R.xml.key);

        // 设置要显示的样式
        this.mKeyboardView.setKeyboard(mKeyBoard);
        this.mKeyboardView.setEnabled(true);
        // 优先使用
        this.mKeyboardView.setPreviewEnabled(false);
        this.mKeyboardView.setOnKeyboardActionListener(mListener);
    }

    KeyboardView.OnKeyboardActionListener mListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable text = mEditText.getText();
            int selectionStart = mEditText.getSelectionStart();
            switch (primaryCode) {
                case Keyboard.KEYCODE_DELETE:
                    if (!TextUtils.isEmpty(text)) {
                        if (selectionStart > 0) {
                            text.delete(selectionStart - 1, selectionStart);
                        }
                    }
                    break;
                case Keyboard.KEYCODE_DONE:
                    mOnKeyBoardListener.onEnsure();
                    break;
                case Keyboard.KEYCODE_CANCEL:
                    text.clear();
                    break;
                default:
                    text.insert(selectionStart, Character.toString((char) primaryCode));
                    break;
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    /**
     * Add keykoard listener.
     * @param onKeyBoardListener
     */
    public void addKeyBoardListener(OnKeyBoardListener onKeyBoardListener) {
        this.mOnKeyBoardListener = onKeyBoardListener;
    }

    /**
     * Show keyboard.
     */
    public void showKeyBoard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.INVISIBLE || visibility == View.GONE) {
            mKeyboardView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Hide keyboard.
     */
    public void hideKeyBoard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.VISIBLE || visibility == View.INVISIBLE) {
            mKeyboardView.setVisibility(View.GONE);
        }
    }
}
