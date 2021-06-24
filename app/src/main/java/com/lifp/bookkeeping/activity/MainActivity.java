package com.lifp.bookkeeping.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lifp.bookkeeping.BookKeepingApplication;
import com.lifp.bookkeeping.R;
import com.lifp.bookkeeping.adapter.RecordAdapter;
import com.lifp.bookkeeping.bean.AccountRecord;
import com.lifp.bookkeeping.bean.Constant;
import com.lifp.bookkeeping.dialog.BudgetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        BudgetDialog.BudgetConfirmListener, RecordAdapter.OnLongClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Button mButton;
    private ImageView mSearch;
    private RecyclerView mRecyclerView;
    private List<AccountRecord> mAccountRecords;
    private RecordAdapter mRecordAdapter;
    private View mAllLayout;
    private TextView mMonthSpend;
    private ImageView mIsShow;
    private TextView mInComeMonth;
    private TextView mBudgetMoneyText;
    private TextView mTodayTotal;
    private TextView mTotalInComeMoney;
    private TextView mTotalOutComeMoney;
    private int mYear;
    private int mMonth;
    private int mDay;
    private boolean mShowMoney;
    private SharedPreferences mSharedPreferences;
    private float mBudgetMoney;
    private BudgetDialog mBudgetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = getPreferences(MODE_PRIVATE);
        initView();
    }

    private void initView() {
        mButton = findViewById(R.id.btn_record);
        mSearch = findViewById(R.id.iv_search);
        mRecyclerView = findViewById(R.id.rv_list);
        mAllLayout = findViewById(R.id.item_top_layout);
        mTotalInComeMoney = findViewById(R.id.item_total_income_money);
        mTotalOutComeMoney = findViewById(R.id.item_total_spend_money);
        mMonthSpend = findViewById(R.id.item_text_top_month_spend_money);
        mIsShow = findViewById(R.id.item_iv_top_show);
        mInComeMonth = findViewById(R.id.item_text_top_month_income_money);
        mBudgetMoneyText = findViewById(R.id.item_text_top_budget_last_money);
        mTodayTotal = findViewById(R.id.item_text_top_total);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAccountRecords = new ArrayList<>();
        mRecordAdapter = new RecordAdapter(this, mAccountRecords);
        mRecordAdapter.setOnLongClickListener(this);
        mRecyclerView.setAdapter(mRecordAdapter);
        mButton.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        mIsShow.setOnClickListener(this);
        mAllLayout.setOnClickListener(this);
        mBudgetMoneyText.setOnClickListener(this);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void onResume() {
        super.onResume();
        showData();
    }

    private void showData() {
        // 获取今天的日期
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        // 每次回到页面要刷新数据
        mAccountRecords.clear();
        mAccountRecords.addAll(BookKeepingApplication.getDataBaseHelper().getAccountRecordListByDate(mYear, mMonth));
        mRecordAdapter.updateData(mAccountRecords);
        // 刷新总支出收入数据
        float totalInCome = BookKeepingApplication.getDataBaseHelper().getTotalMoneyByKind(Constant.IN_COME);
        float totalOutCome = BookKeepingApplication.getDataBaseHelper().getTotalMoneyByKind(Constant.OUT_COME);
        mTotalInComeMoney.setText("￥ " + totalInCome);
        mTotalOutComeMoney.setText("￥ " + totalOutCome);
        // 刷新每日支出收入数据
        float dayOutCome = BookKeepingApplication.getDataBaseHelper().getDailyMoneyByKind(mYear, mMonth, mDay,
                Constant.OUT_COME);
        float dayInCome = BookKeepingApplication.getDataBaseHelper().getDailyMoneyByKind(mYear, mMonth, mDay,
                Constant.IN_COME);
        Log.d(TAG, "onResume dayOutCome:" + dayOutCome + ",dayInCome:" + dayInCome);
        mTodayTotal.setText(String.format("%s%s", "今日支出 ￥" + dayOutCome, " 收入 ￥" + dayInCome));
        // 刷新每月支出收入数据
        float monthOutCome = BookKeepingApplication.getDataBaseHelper().getMonthMoneyByKind(mYear, mMonth,
                Constant.OUT_COME);
        float monthInCome = BookKeepingApplication.getDataBaseHelper().getMonthMoneyByKind(mYear, mMonth,
                Constant.IN_COME);
        Log.d(TAG, "onResume monthOutCome:" + monthOutCome + ",monthInCome:" + monthInCome);
        mMonthSpend.setText("￥ " + monthOutCome);
        mInComeMonth.setText("￥ " + monthInCome);

        // 显示预算剩余金额
        mBudgetMoney = mSharedPreferences.getFloat(Constant.BUDGET_MONEY_KEY, 0);
        float monthMoney = BookKeepingApplication.getDataBaseHelper().getMonthMoneyByKind(mYear, mMonth,
                Constant.OUT_COME);
        if (mBudgetMoney != 0) {
            mBudgetMoneyText.setText("￥ " + (mBudgetMoney - monthMoney));
        }
    }

    /**
     * 切换主页顶部金额明文密文
     */
    private void toggleShow(){
        if (mShowMoney) {
            PasswordTransformationMethod method = PasswordTransformationMethod.getInstance();
            mMonthSpend.setTransformationMethod(method);
            mInComeMonth.setTransformationMethod(method);
            mBudgetMoneyText.setTransformationMethod(method);
            mTotalInComeMoney.setTransformationMethod(method);
            mTotalOutComeMoney.setTransformationMethod(method);
            mIsShow.setImageResource(R.mipmap.ih_hide);
            mShowMoney = false;
        } else {
            HideReturnsTransformationMethod method = HideReturnsTransformationMethod.getInstance();
            mMonthSpend.setTransformationMethod(method);
            mInComeMonth.setTransformationMethod(method);
            mBudgetMoneyText.setTransformationMethod(method);
            mTotalInComeMoney.setTransformationMethod(method);
            mTotalOutComeMoney.setTransformationMethod(method);
            mIsShow.setImageResource(R.mipmap.ih_show);
            mShowMoney = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_record:
                Intent intent = new Intent(this, RecordActivity.class);
                startActivity(intent);
                break;
            case R.id.item_iv_top_show:
                toggleShow();
                break;
            case R.id.item_text_top_budget_last_money:
                showBudgetDialog();
                break;
            case R.id.iv_search:
                Intent tempIntent = new Intent(this, SearchActivity.class);
                startActivity(tempIntent);
                break;
            case R.id.item_top_layout:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            default:
                break;
        }
    }

    private void showBudgetDialog() {
        mBudgetDialog = new BudgetDialog(this);
        mBudgetDialog.setCancelable(false);
        mBudgetDialog.setCanceledOnTouchOutside(false);
        mBudgetDialog.setBudgetConfirmListener(this);
        mBudgetDialog.show();
    }

    @Override
    public void onConfirm(float money) {
        Log.d(TAG, "onConfirm budget money:" + money);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(Constant.BUDGET_MONEY_KEY, money);
        editor.apply();
        float monthMoney = BookKeepingApplication.getDataBaseHelper().getMonthMoneyByKind(mYear, mMonth,
                Constant.OUT_COME);
        mBudgetMoneyText.setText("￥ " + (money - monthMoney));
    }

    @Override
    public void onLongClick(int position) {
        Log.d(TAG, "onLongClick:" + position);
        AccountRecord bean = mAccountRecords.get(position);
        showDeleteDialog(bean);
    }

    private void showDeleteDialog(AccountRecord accountRecord) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息")
                .setMessage("您确定要删除这条记录吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean result = BookKeepingApplication.getDataBaseHelper().deleteById(accountRecord.getId());
                        mAccountRecords.remove(accountRecord);
                        Log.d(TAG, "delete :" + result);
                        mRecordAdapter.notifyDataSetChanged();
                        showData();
                    }
                }).create().show();
    }
}