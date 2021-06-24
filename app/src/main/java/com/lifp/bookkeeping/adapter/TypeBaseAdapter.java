package com.lifp.bookkeeping.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lifp.bookkeeping.R;
import com.lifp.bookkeeping.bean.Type;

import java.util.ArrayList;
import java.util.List;

public class TypeBaseAdapter extends BaseAdapter {
    private Context mContext;
    private List<Type> mTypeList = new ArrayList<>();
    private int mSelectedPosition = 0;

    public TypeBaseAdapter(Context context, List<Type> typeList) {
        mContext = context;
        mTypeList = typeList;
    }

    @Override
    public int getCount() {
        return mTypeList != null ? mTypeList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 此适配器不需要考虑复用的情况,因为所有的页面都是显示在界面上,不会因为滑动就消失.
     * @param position .
     * @param convertView .
     * @param parent .
     * @return .
     */
    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gridview, parent,false);
        ImageView iv = convertView.findViewById(R.id.item_gridview_iv);
        TextView tv = convertView.findViewById(R.id.item_gridview_tv);
        Type type = mTypeList.get(position);
        tv.setText(type.getTypeName());
        iv.setImageResource(type.getImageSelected());
        return convertView;
    }

    /**
     * set up click position
     *
     * @Author lifp
     * @Date 21-1-22
     * @param: position
     */
    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
        notifyDataSetInvalidated();
    }
}