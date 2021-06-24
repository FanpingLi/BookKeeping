package com.lifp.bookkeeping.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecordPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();

    /**
     * 构造器
     * @param fm Fragment管理
     * @param list Fragment列表
     * @param titleList 标题列表
     */
    public RecordPagerAdapter(@NonNull FragmentManager fm, List<Fragment> list, List<String> titleList) {
        super(fm);
        mList = list;
        mTitleList = titleList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }
}
