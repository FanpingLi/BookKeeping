package com.lifp.bookkeeping.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lifp.bookkeeping.R;
import com.lifp.bookkeeping.bean.Constant;

public class BudgetDialog extends Dialog implements View.OnClickListener {

    private ImageView mCancel;
    private Button mConfirm;
    private EditText mMoney;
    private BudgetConfirmListener mBudgetConfirmListener;

    public BudgetDialog(@NonNull Context context) {
        super(context);
    }

    public void setBudgetConfirmListener( BudgetConfirmListener listener) {
        mBudgetConfirmListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_budget_layout);
        mCancel = findViewById(R.id.iv_budget_dialog_close);
        mConfirm = findViewById(R.id.btn_budget_dialog_confirm);
        mMoney = findViewById(R.id.et_budget_dialog_money);

        mConfirm.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = Constant.BUDGET_DIALOG_WIDTH;
            params.height = Constant.BUDGET_DIALOG_HEIGHT;
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_budget_dialog_close:
                BudgetDialog.this.dismiss();
                break;
            case R.id.btn_budget_dialog_confirm:
                String data = mMoney.getText().toString();
                if (TextUtils.isEmpty(data)) {
                    Toast.makeText(getContext(), "预算金额不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                float money = Float.parseFloat(data);
                if (money <= 0) {
                    Toast.makeText(getContext(), "预算金额必须大于0", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mBudgetConfirmListener != null) {
                    mBudgetConfirmListener.onConfirm(money);
                }
                BudgetDialog.this.dismiss();
                break;
            default:
                break;
        }
    }

    public interface BudgetConfirmListener {
        void onConfirm(float money);
    }
}
