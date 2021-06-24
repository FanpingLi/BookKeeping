package com.lifp.bookkeeping.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lifp.bookkeeping.R;
import com.lifp.bookkeeping.adapter.RecordPagerAdapter;
import com.lifp.bookkeeping.fragment.IncomeFragment;
import com.lifp.bookkeeping.fragment.OutcomeFragment;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private RecordPagerAdapter mRecordPagerAdapter;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initView();
        initFragment();
    }

    private void initView() {
        mTabLayout = findViewById(R.id.layout_tab);
        mViewPager = findViewById(R.id.layou_viewpager);
        mImageView = findViewById(R.id.iv_record_close);
        mImageView.setOnClickListener(this);
    }

    private void initFragment() {
        List<Fragment> fragmentList = new ArrayList<>();
        IncomeFragment incomeFragment = new IncomeFragment();
        OutcomeFragment outcomeFragment = new OutcomeFragment();
        fragmentList.add(outcomeFragment);
        fragmentList.add(incomeFragment);
        List<String> titleList = new ArrayList<>();
        titleList.add(getResources().getString(R.string.spend));
        titleList.add(getResources().getString(R.string.income));

        mRecordPagerAdapter = new RecordPagerAdapter(
                getSupportFragmentManager(), fragmentList, titleList);
        mViewPager.setAdapter(mRecordPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_record_close:
                finish();
                break;
            default:
                break;
        }
    }
}