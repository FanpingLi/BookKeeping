package com.lifp.bookkeeping.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lifp.bookkeeping.BookKeepingApplication;
import com.lifp.bookkeeping.R;
import com.lifp.bookkeeping.adapter.RecordAdapter;
import com.lifp.bookkeeping.bean.AccountRecord;
import com.lifp.bookkeeping.dialog.HistoryDateDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener, HistoryDateDialog.HistoryConfirmListener {

    private ImageView mBack;
    private ImageView mSelectDate;
    private TextView mDate;
    private RecyclerView mRecyclerView;
    private RecordAdapter mRecordAdapter;
    private List<AccountRecord> mData = new ArrayList<>();
    private int mYear;
    private int mMonth;
    private HistoryDateDialog mHistoryDateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        initView();
        updateData(mYear, mMonth);
    }

    private void updateData(int year, int month) {
        mData = BookKeepingApplication.getDataBaseHelper().getAccountRecordListByDate(year, month);
        mRecordAdapter.updateData(mData);
        mDate.setText(year + "年" + month + "月");
    }

    private void initView() {
        mBack = findViewById(R.id.iv_back);
        mSelectDate = findViewById(R.id.iv_date);
        mDate = findViewById(R.id.history_date);
        mRecyclerView = findViewById(R.id.rv_history);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecordAdapter = new RecordAdapter(this, mData);
        mRecyclerView.setAdapter(mRecordAdapter);

        mBack.setOnClickListener(this);
        mSelectDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_date:
                // select date
                mHistoryDateDialog = new HistoryDateDialog(this);
                mHistoryDateDialog.setCancelable(false);
                mHistoryDateDialog.setCanceledOnTouchOutside(false);
                mHistoryDateDialog.setHistoryConfirmListener(this);
                mHistoryDateDialog.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onConfirm(int year, int month) {
        updateData(year, month);
    }
}
