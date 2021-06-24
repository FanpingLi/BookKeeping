package com.lifp.bookkeeping.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.lifp.bookkeeping.R;

public class CommentDialog extends Dialog implements View.OnClickListener {

    private EditText mEditText;
    private Button mConfirm;
    private Button mCancel;
    private InputMethodManager mInputManager;
    private OnConfirmListener mOnConfirmListener;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            mInputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment);

        mEditText = findViewById(R.id.dialog_comment_et);
        mConfirm = findViewById(R.id.btn_confirm);
        mCancel = findViewById(R.id.btn_cancel);

        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);

        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        Display defaultDisplay = getWindow().getWindowManager().getDefaultDisplay();
        attributes.width = defaultDisplay.getWidth();
        attributes.gravity = Gravity.BOTTOM;
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setAttributes(attributes);
    }

    @Override
    public void show() {
        super.show();
        if (mInputManager != null) {
            mEditText.setFocusable(true);
            mEditText.setFocusableInTouchMode(true);
            mEditText.requestFocus();
            // 不延时显示 软键盘会闪一下然后消失，原因未知。
            mHandler.sendEmptyMessageDelayed(1, 100);
        }
    }

    public CommentDialog(@NonNull Context context) {
        super(context);
        mInputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                cancel();
                break;
            case R.id.btn_confirm:
                if (mOnConfirmListener != null) {
                    mOnConfirmListener.onConfirm();
                }
                break;
            default:
                break;
        }
    }

    public String getComment() {
        return mEditText.getText().toString().trim();
    }

    public void setComment(String comment) {
        mEditText.setText(comment);
        mEditText.setSelection(comment.length());
    }

    public void addConfirmListener(OnConfirmListener onConfirmListener) {
        mOnConfirmListener = onConfirmListener;
    }

    public interface OnConfirmListener{

        void onConfirm();
    }
}
