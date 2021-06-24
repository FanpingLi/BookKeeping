package com.lifp.bookkeeping.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

import com.lifp.bookkeeping.R;
import com.lifp.bookkeeping.bean.Constant;

import java.util.Calendar;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class HistoryDateDialog extends Dialog implements NumberPicker.OnValueChangeListener, View.OnClickListener {

    private NumberPicker mYear;
    private NumberPicker mMonth;
    private ImageView mClose;
    private Button mConfirm;
    private HistoryConfirmListener mHistoryConfirmListener;

    public HistoryDateDialog(@NonNull Context context) {
        super(context);
    }

    public void setHistoryConfirmListener(HistoryConfirmListener listener) {
        mHistoryConfirmListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_history_date);
        mYear = findViewById(R.id.number_year);
        mMonth = findViewById(R.id.number_month);
        mClose = findViewById(R.id.iv_close);
        mConfirm = findViewById(R.id.bt_confirm);

        mClose.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mYear.setOnValueChangedListener(this);
        mMonth.setOnValueChangedListener(this);

        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = MATCH_PARENT;
            params.height = Constant.HISTORY_DIALOG_HEIGHT;
            params.gravity = Gravity.BOTTOM;
            window.setAttributes(params);
        }
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        mYear.setMaxValue(year + 20);
        mYear.setMinValue(year - 20);
        mYear.setValue(year);
        mYear.setWrapSelectorWheel(false);

        mMonth.setMaxValue(12);
        mMonth.setMinValue(1);
        mMonth.setValue(calendar.get(Calendar.MONTH) + 1);
        mMonth.setWrapSelectorWheel(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                HistoryDateDialog.this.dismiss();
                break;
            case R.id.bt_confirm:
                if (mHistoryConfirmListener != null) {
                    mHistoryConfirmListener.onConfirm(mYear.getValue(), mMonth.getValue());
                }
                HistoryDateDialog.this.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    public interface HistoryConfirmListener{
        void onConfirm(int year, int month);
    }
}
