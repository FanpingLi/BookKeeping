package com.lifp.bookkeeping.dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.lifp.bookkeeping.R;
import com.lifp.bookkeeping.listener.OnDatePickerListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerDialogFragment extends DialogFragment implements NumberPicker.OnValueChangeListener,
        View.OnClickListener {

    private static final String TAG = DatePickerDialogFragment.class.getSimpleName();

    private NumberPicker mYearPicker;
    private NumberPicker mMonthPicker;
    private NumberPicker mDayPicker;
    private NumberPicker mHourPicker;
    private NumberPicker mMinutePicker;
    private Button mCancel;
    private Button mConfirm;
    private OnDatePickerListener mListener;

    public DatePickerDialogFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time_date_picker_layout, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mYearPicker = view.findViewById(R.id.date_year);
        mMonthPicker = view.findViewById(R.id.date_month);
        mDayPicker = view.findViewById(R.id.date_day);
        mHourPicker = view.findViewById(R.id.date_hour);
        mMinutePicker = view.findViewById(R.id.date_minute);
        mCancel = view.findViewById(R.id.btn_picker_cancel);
        mConfirm = view.findViewById(R.id.btn_picker_confirm);

        mYearPicker.setOnValueChangedListener(this);
        mMonthPicker.setOnValueChangedListener(this);
        mConfirm.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        mYearPicker.setMaxValue(year + 20);
        mYearPicker.setMinValue(year - 20);
        mYearPicker.setValue(year);
        // 关闭选择器循环
        mYearPicker.setWrapSelectorWheel(false);

        mMonthPicker.setMaxValue(12);
        mMonthPicker.setMinValue(1);
        mMonthPicker.setValue(calendar.get(Calendar.MONTH) + 1);
        mMonthPicker.setWrapSelectorWheel(false);

        mDayPicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        mDayPicker.setMinValue(1);
        mDayPicker.setValue(calendar.get(Calendar.DATE));
        mDayPicker.setWrapSelectorWheel(false);

        mHourPicker.setMinValue(0);
        mHourPicker.setMaxValue(23);
        mHourPicker.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        mHourPicker.setWrapSelectorWheel(false);

        mMinutePicker.setMinValue(0);
        mMinutePicker.setMaxValue(59);
        mMinutePicker.setValue(calendar.get(Calendar.MINUTE));
        mMinutePicker.setWrapSelectorWheel(false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        String str = String.format(Locale.CHINA, "%d-%d", mYearPicker.getValue(), mMonthPicker.getValue());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(str));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        int dayValue = mDayPicker.getValue();
        int maxValue = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        mDayPicker.setMaxValue(maxValue);
        mDayPicker.setValue(Math.min(dayValue, maxValue));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_picker_cancel:
                dismissAllowingStateLoss();
                break;
            case R.id.btn_picker_confirm:
                long millis = formatTime();
                if (mListener != null) {
                    mListener.onConfirm(millis, mYearPicker.getValue(), mMonthPicker.getValue(), mDayPicker.getValue());
                }
                dismissAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    public void setOnDatePickerListener(OnDatePickerListener listener) {
        mListener = listener;
    }

    private long formatTime() {
        int year = mYearPicker.getValue();
        int month = mMonthPicker.getValue();
        int day = mDayPicker.getValue();
        int hour = mHourPicker.getValue();
        int minute = mMinutePicker.getValue();

        Log.d(TAG, "year:" + year + "month:" + month + "day:" + day + "hour:" + hour + "minute:" + minute);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND));

        return calendar.getTimeInMillis();
    }
}
