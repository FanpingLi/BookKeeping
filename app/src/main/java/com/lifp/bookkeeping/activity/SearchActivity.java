package com.lifp.bookkeeping.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lifp.bookkeeping.BookKeepingApplication;
import com.lifp.bookkeeping.R;
import com.lifp.bookkeeping.adapter.RecordAdapter;
import com.lifp.bookkeeping.bean.AccountRecord;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SearchActivity.class.getSimpleName();
    private ImageView mBack;
    private ImageView mClear;
    private EditText mEditText;
    private ImageView mSearch;
    private RecyclerView mRecyclerView;
    private RecordAdapter mRecordAdapter;
    private List<AccountRecord> mRecords = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        mBack = findViewById(R.id.iv_back);
        mClear = findViewById(R.id.iv_clear);
        mEditText = findViewById(R.id.et_search);
        mRecyclerView = findViewById(R.id.rv_data);
        mSearch = findViewById(R.id.iv_search);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecordAdapter = new RecordAdapter(this, mRecords);
        mRecyclerView.setAdapter(mRecordAdapter);
        mBack.setOnClickListener(this);
        mClear.setOnClickListener(this);
        mSearch.setOnClickListener(this);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // empty
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRecords.clear();
                if (!TextUtils.isEmpty(s)) {
                    mClear.setVisibility(View.VISIBLE);
                    getListByComment();
                } else {
                    mClear.setVisibility(View.GONE);
                    mRecordAdapter.updateData(mRecords);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //empty
            }
        });

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d(TAG, "onEditorAction :" + actionId);
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getListByComment();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_clear:
                mEditText.setText("");
                break;
            case R.id.iv_search:
                if (!TextUtils.isEmpty(mEditText.getText().toString())) {
                    getListByComment();
                } else {
                    Toast.makeText(this, "请输入搜索内容", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    private void getListByComment() {
        Log.d(TAG, "msg:" + mEditText.getText().toString());
        mRecords = BookKeepingApplication.getDataBaseHelper().getListByComment(mEditText.getText().toString());
        mRecordAdapter.updateData(mRecords);
    }
}
