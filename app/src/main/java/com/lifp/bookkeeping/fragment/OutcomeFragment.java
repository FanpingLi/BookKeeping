package com.lifp.bookkeeping.fragment;

import android.bluetooth.BluetoothAdapter;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lifp.bookkeeping.BookKeepingApplication;
import com.lifp.bookkeeping.R;
import com.lifp.bookkeeping.adapter.TypeBaseAdapter;
import com.lifp.bookkeeping.bean.AccountRecord;
import com.lifp.bookkeeping.bean.Constant;
import com.lifp.bookkeeping.bean.Type;
import com.lifp.bookkeeping.dialog.DatePickerDialogFragment;
import com.lifp.bookkeeping.listener.OnKeyBoardListener;
import com.lifp.bookkeeping.utils.CommentDialog;
import com.lifp.bookkeeping.utils.KeyBoardUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 记录页面支出页面
 */
public class OutcomeFragment extends Fragment {

    private static final String TAG = OutcomeFragment.class.getSimpleName();
    private KeyboardView mKeyboardView;
    private ImageView mRecordType;
    private TextView mRecordTypeText;
    private EditText mRecordMoney;
    private TextView mRecordNote;
    private TextView mRecordTime;
    private GridView mGridView;
    private TypeBaseAdapter mTypeBaseAdapter;
    private AccountRecord mAccountRecord;
    private DatePickerDialogFragment mDatePickerDialogFragment;
    private List<Type> mOutList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outincome, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mKeyboardView = view.findViewById(R.id.fragment_keyboard);
        mRecordType = view.findViewById(R.id.fragment_iv_record_type);
        mRecordTypeText = view.findViewById(R.id.fragment_text_record_type);
        mRecordMoney = view.findViewById(R.id.fragment_edit_record_money);
        mRecordNote = view.findViewById(R.id.fragment_note);
        mRecordTime = view.findViewById(R.id.fragment_time);
        mGridView = view.findViewById(R.id.fragment_gridview);

        // 显示键盘
        KeyBoardUtils keyBoardUtils = new KeyBoardUtils(mKeyboardView, mRecordMoney);
        keyBoardUtils.showKeyBoard();
        // 监听确定按钮被点击了
        keyBoardUtils.addKeyBoardListener(new OnKeyBoardListener() {
            @Override
            public void onEnsure() {
                String money = mRecordMoney.getText().toString();
                if (TextUtils.isEmpty(money) || "0".equals(money)) {
                    Toast.makeText(getContext(), "请输入金额！", Toast.LENGTH_SHORT).show();
                }else {
                    mAccountRecord.setMoney(Float.parseFloat(money));
                    mAccountRecord.setComment(mRecordNote.getText().toString());
                    BookKeepingApplication.getDataBaseHelper().insert(mAccountRecord);

                    getActivity().finish();
                }
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Type type = mOutList.get(position);
                mTypeBaseAdapter.setSelectedPosition(position);
                mRecordType.setImageResource(type.getImageSelected());
                mRecordTypeText.setText(type.getTypeName());

                mAccountRecord.setTypeName(type.getTypeName());
                mAccountRecord.setType(type.getKind());
                mAccountRecord.setSlectedImage(type.getImageSelected());
            }
        });

        mRecordNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentDialog();
            }
        });

        mRecordTime.setOnClickListener( v -> {
            mDatePickerDialogFragment = new DatePickerDialogFragment();
            mDatePickerDialogFragment.show(getChildFragmentManager(), DatePickerDialogFragment.class.getCanonicalName());
            mDatePickerDialogFragment.setOnDatePickerListener((millis, year, month, day) -> {
                Log.d(TAG, "millis:" + millis);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                String format = sdf.format(millis);
                mRecordTime.setText(format);
                mAccountRecord.setYear(year);
                mAccountRecord.setMonth(month);
                mAccountRecord.setDay(day);
                mAccountRecord.setTime(format);
            });
        });
    }

    private void showCommentDialog() {
        final CommentDialog dialog = new CommentDialog(getContext());
        String comment = mRecordNote.getText().toString().trim();
        dialog.show();

        if (!TextUtils.isEmpty(comment)) {
            dialog.setComment(comment);
        }

        dialog.addConfirmListener(new CommentDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                String comment = dialog.getComment();
                if (!TextUtils.isEmpty(comment)) {
                    mRecordNote.setText(comment);
                    mAccountRecord.setComment(comment);
                    dialog.dismiss();
                }else {
                    Toast.makeText(getContext(), "备注不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData() {
        mAccountRecord = new AccountRecord();
        mOutList = BookKeepingApplication.getDataBaseHelper().getTypeList(Constant.OUT_COME);
        mTypeBaseAdapter = new TypeBaseAdapter(getContext(), mOutList);
        mGridView.setAdapter(mTypeBaseAdapter);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String format = sdf.format(date);
        mRecordTime.setText(format);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mAccountRecord.setSlectedImage(Constant.DEFAULT_OUT_IMAGE);
        mAccountRecord.setTypeName(Constant.DEFAULT_TYPE);
        mAccountRecord.setTime(format);
        mAccountRecord.setYear(year);
        mAccountRecord.setMonth(month);
        mAccountRecord.setDay(day);
        mAccountRecord.setType(Constant.OUT_COME);
    }
}
