package com.lifp.bookkeeping.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lifp.bookkeeping.BookKeepingApplication;
import com.lifp.bookkeeping.R;
import com.lifp.bookkeeping.bean.AccountRecord;
import com.lifp.bookkeeping.bean.Constant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordHolder> {

    private Context mContext;
    private List<AccountRecord> mAccountRecords = new ArrayList<>();
    private OnLongClickListener mOnLongClickListener;

    public RecordAdapter(Context context, List<AccountRecord> accountRecords) {
        mContext = context;
        mAccountRecords.clear();
        mAccountRecords.addAll(accountRecords);
    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(BookKeepingApplication.getContext()).inflate(R.layout.item_rv, parent, false);
        return new RecordHolder(view);
    }

    public void setOnLongClickListener(OnLongClickListener listener) {
        mOnLongClickListener = listener;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.RecordHolder holder, int position) {
        AccountRecord accountRecord = getItem(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnLongClickListener != null) {
                    mOnLongClickListener.onLongClick(position);
                }
                return true;
            }
        });
        if (accountRecord != null) {
            holder.mImageView.setImageResource(accountRecord.getSlectedImage());
            holder.mType.setText(accountRecord.getTypeName());
            holder.mNote.setText(accountRecord.getComment());
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.YEAR) == accountRecord.getYear() && calendar.get(Calendar.MONTH) + 1 == accountRecord.getMonth() && calendar.get(Calendar.DAY_OF_MONTH) == accountRecord.getDay()) {
                String str = accountRecord.getTime().split(" ")[1];
                holder.mDate.setText("今天 " + str);
            } else {
                holder.mDate.setText(accountRecord.getTime());
            }
            if (accountRecord.getType() == Constant.IN_COME) {
                holder.mMoney.setText("￥ +" + String.valueOf(accountRecord.getMoney()));
            } else {
                holder.mMoney.setText("￥ -" + String.valueOf(accountRecord.getMoney()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mAccountRecords != null ? mAccountRecords.size() : 0;
    }

    public AccountRecord getItem(int position) {
        if (mAccountRecords != null && mAccountRecords.size() > position) {
            return mAccountRecords.get(position);
        }
        return null;
    }

    public void updateData(List<AccountRecord> list) {
        if (list != null) {
            mAccountRecords.clear();
            mAccountRecords.addAll(list);
            notifyDataSetChanged();
        }
    }

    public static class RecordHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;
        private TextView mType;
        private TextView mNote;
        private TextView mMoney;
        private TextView mDate;
        private OnLongClickListener mOnLongClickListener;

        public RecordHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_image);
            mType = itemView.findViewById(R.id.item_type);
            mNote = itemView.findViewById(R.id.item_note);
            mMoney = itemView.findViewById(R.id.item_money);
            mDate = itemView.findViewById(R.id.item_date);
        }
    }

    public interface OnLongClickListener {
        void onLongClick(int position);
    }
}
