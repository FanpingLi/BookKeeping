package com.lifp.bookkeeping.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.Nullable;

import com.lifp.bookkeeping.BookKeepingApplication;
import com.lifp.bookkeeping.R;
import com.lifp.bookkeeping.bean.AccountRecord;
import com.lifp.bookkeeping.bean.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作工具类.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DataBaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "tally.db";
    private static final int DATABASE_BASIC_VERSION = 1;
    private static final String TYPE_TABLE_NAME = "record_type";
    private static final String ACCOUNT_TABLE_NAME = "account";
    private static final int OUTCOME_KIND = 0;
    private static final int INCOME_KIND = 1;
    private static final int[] OUTCOME_IMAGE_SELECTED = new int[]{R.mipmap.ic_qita, R.mipmap.ic_yinshi,
            R.mipmap.ic_jiaotong, R.mipmap.ic_gouwu, R.mipmap.ic_fushi, R.mipmap.ic_riyongpin, R.mipmap.ic_yanjiuchashui,
            R.mipmap.ic_shuidianfei, R.mipmap.ic_lingshi, R.mipmap.ic_lvyou, R.mipmap.ic_huazhuangpin,
            R.mipmap.ic_huafei, R.mipmap.ic_yule, R.mipmap.ic_xuexi, R.mipmap.ic_yiliao, R.mipmap.ic_youxi,
            R.mipmap.ic_haizi, R.mipmap.ic_baoxian, R.mipmap.ic_chongwu, R.mipmap.ic_jianshen, R.mipmap.ic_zhuzhai,
            R.mipmap.ic_renqingwanglai};
    private static final int[] INCOME_IMAGE_SELECTED = new int[]{R.mipmap.ic_qita, R.mipmap.ic_gongzi,
            R.mipmap.ic_jiangjin, R.mipmap.ic_shouzhai, R.mipmap.ic_ershoujiaoyi, R.mipmap.ic_liwulijin,
            R.mipmap.ic_lixi, R.mipmap.ic_jieru, R.mipmap.ic_jijin, R.mipmap.ic_jianzhi};

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_BASIC_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TYPE_TABLE_NAME + "("
                + TallyColums.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TallyColums.TYPE_NAME + " TEXT,"
                + TallyColums.IMAGE_SELECTED_ID + " INTEGER,"
                + TallyColums.KIND + " INTEGER)");

        db.execSQL("CREATE TABLE " + ACCOUNT_TABLE_NAME + "("
                + AccountColums.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + AccountColums.TYPE_NAME + " TEXT,"
                + AccountColums.SELECTED_IMAGE + " INTEGER,"
                + AccountColums.COMMENT + " TEXT,"
                + AccountColums.MONEY + " FLOAT,"
                + AccountColums.TIME + " TEXT,"
                + AccountColums.YEAR + " INTEGER,"
                + AccountColums.MONTH + " INTEGER,"
                + AccountColums.DAY + " INTEGER,"
                + AccountColums.TYPE + " INTEGER)");

        String[] outTypeText =
                BookKeepingApplication.getContext().getResources().getStringArray(R.array.outcome_type_text);
        for (int i = 0; i < outTypeText.length; i++) {
            db.execSQL("INSERT INTO " + TYPE_TABLE_NAME + "("
                    + TallyColums.TYPE_NAME + ","
                    + TallyColums.IMAGE_SELECTED_ID + ","
                    + TallyColums.KIND + ") VALUES('"
                    + outTypeText[i] + "','" + OUTCOME_IMAGE_SELECTED[i]
                    + "','" + OUTCOME_KIND + "');"
            );
        }
        String[] intTypeText =
                BookKeepingApplication.getContext().getResources().getStringArray(R.array.income_type_text);
        for (int i = 0; i < intTypeText.length; i++) {
            db.execSQL("INSERT INTO " + TYPE_TABLE_NAME + "("
                    + TallyColums.TYPE_NAME + ","
                    + TallyColums.IMAGE_SELECTED_ID + ","
                    + TallyColums.KIND + ") VALUES('"
                    + intTypeText[i] + "','" + INCOME_IMAGE_SELECTED[i]
                    + "','" + INCOME_KIND + "');"
            );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 图片资源
     */
    private static final class TallyColums implements BaseColumns {
        public static final String ID = "id";
        public static final String TYPE_NAME = "type_name";
        public static final String IMAGE_SELECTED_ID = "image_selected_id";
        public static final String KIND = "kind";
    }

    /**
     * 记账库
     */
    private static final class AccountColums implements BaseColumns {
        public static final String ID = "id";
        public static final String TYPE_NAME = "type_name";
        public static final String SELECTED_IMAGE = "selected_image";
        public static final String COMMENT = "comment";
        public static final String MONEY = "money";
        public static final String TIME = "time";
        public static final String YEAR = "year";
        public static final String MONTH = "month";
        public static final String DAY = "day";
        public static final String TYPE = "type";
    }

    /**
     * 根据kind获取记账类型列表.
     *
     * @param kind 记账类型, 0-支出 1-收入
     * @return 记账类型列表
     */
    public List<Type> getTypeList(int kind) {
        List<Type> typeList = new ArrayList<>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery(
                    "select * from " + TYPE_TABLE_NAME
                            + " where " + TallyColums.KIND + " = " + kind, null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(TallyColums.ID));
                String typeName = cursor.getString(cursor.getColumnIndex(TallyColums.TYPE_NAME));
                int imageSelected = cursor.getInt(cursor.getColumnIndex(TallyColums.IMAGE_SELECTED_ID));
                int typeKind = cursor.getInt(cursor.getColumnIndex(TallyColums.KIND));
                Type type = new Type(id, typeName, imageSelected, typeKind);
                typeList.add(type);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return typeList;
    }

    /**
     * 向记账表插入数据
     */
    public void insert(AccountRecord accountRecord) {
        ContentValues content = new ContentValues();
        content.put(AccountColums.TYPE_NAME, accountRecord.getTypeName());
        content.put(AccountColums.SELECTED_IMAGE, accountRecord.getSlectedImage());
        content.put(AccountColums.COMMENT, accountRecord.getComment());
        content.put(AccountColums.MONEY, accountRecord.getMoney());
        content.put(AccountColums.TIME, accountRecord.getTime());
        content.put(AccountColums.YEAR, accountRecord.getYear());
        content.put(AccountColums.MONTH, accountRecord.getMonth());
        content.put(AccountColums.DAY, accountRecord.getDay());
        content.put(AccountColums.TYPE, accountRecord.getType());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(ACCOUNT_TABLE_NAME, null, content);
        Log.d(TAG, "insert AccountRecord success");
    }

    public List<AccountRecord> getAccountRecordListByDate(int year, int month, int day) {
        List<AccountRecord> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor =
                    db.rawQuery("select * from " + ACCOUNT_TABLE_NAME + " where " + AccountColums.YEAR + " = " + year + " and " + AccountColums.MONTH + " = " + month + " and " + AccountColums.DAY + " = " + day + " order by " + AccountColums.TIME + " DESC", null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(AccountColums.ID));
                String typeName = cursor.getString(cursor.getColumnIndex(AccountColums.TYPE_NAME));
                int imageId = cursor.getInt(cursor.getColumnIndex(AccountColums.SELECTED_IMAGE));
                String comment = cursor.getString(cursor.getColumnIndex(AccountColums.COMMENT));
                float money = cursor.getFloat(cursor.getColumnIndex(AccountColums.MONEY));
                String time = cursor.getString(cursor.getColumnIndex(AccountColums.TIME));
                int yearTemp = cursor.getInt(cursor.getColumnIndex(AccountColums.YEAR));
                int monthTemp = cursor.getInt(cursor.getColumnIndex(AccountColums.MONTH));
                int dayTemp = cursor.getInt(cursor.getColumnIndex(AccountColums.DAY));
                int type = cursor.getInt(cursor.getColumnIndex(AccountColums.TYPE));
                AccountRecord accountRecord = new AccountRecord(id,typeName,imageId,comment,money,time,yearTemp,monthTemp
                        ,dayTemp,type);
                list.add(accountRecord);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public List<AccountRecord> getAccountRecordList() {
        List<AccountRecord> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor =
                    db.rawQuery("select * from " + ACCOUNT_TABLE_NAME, null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(AccountColums.ID));
                String typeName = cursor.getString(cursor.getColumnIndex(AccountColums.TYPE_NAME));
                int imageId = cursor.getInt(cursor.getColumnIndex(AccountColums.SELECTED_IMAGE));
                String comment = cursor.getString(cursor.getColumnIndex(AccountColums.COMMENT));
                float money = cursor.getFloat(cursor.getColumnIndex(AccountColums.MONEY));
                String time = cursor.getString(cursor.getColumnIndex(AccountColums.TIME));
                int yearTemp = cursor.getInt(cursor.getColumnIndex(AccountColums.YEAR));
                int monthTemp = cursor.getInt(cursor.getColumnIndex(AccountColums.MONTH));
                int dayTemp = cursor.getInt(cursor.getColumnIndex(AccountColums.DAY));
                int type = cursor.getInt(cursor.getColumnIndex(AccountColums.TYPE));
                AccountRecord accountRecord = new AccountRecord(id,typeName,imageId,comment,money,time,yearTemp,monthTemp
                        ,dayTemp,type);
                list.add(accountRecord);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 获取某一天的支出或收入的总金额
     * @param kind 0~支出，1~收入
     * @return 总金额
     */
    public float getDailyMoneyByKind(int year, int month, int day, int kind) {
        float money = 0;
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery("select sum(money) from " + ACCOUNT_TABLE_NAME + " where "
                            + AccountColums.YEAR + " = " + year + " and "
                            + AccountColums.MONTH + " = " + month + " and "
                            + AccountColums.DAY + " = " + day + " and "
                            + AccountColums.TYPE + " = " + kind,
                    null);
            if (cursor.moveToFirst()) {
                money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return money;
    }

    /**
     * 获取某月的支出或收入的总金额
     * @param kind 0~支出，1~收入
     * @return 总金额
     */
    public float getMonthMoneyByKind(int year, int month, int kind) {
        float money = 0;
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery("select sum(money) from " + ACCOUNT_TABLE_NAME + " where "
                            + AccountColums.YEAR + " = " + year + " and "
                            + AccountColums.MONTH + " = " + month + " and "
                            + AccountColums.TYPE + " = " + kind,
                    null);
            if (cursor.moveToFirst()) {
                money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return money;
    }

    public List<AccountRecord> getAccountRecordListByDate(int year, int month) {
        List<AccountRecord> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor =
                    db.rawQuery("select * from " + ACCOUNT_TABLE_NAME + " where " + AccountColums.YEAR + " = " + year + " and " + AccountColums.MONTH + " = " + month + " order by " + AccountColums.TIME + " DESC", null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(AccountColums.ID));
                String typeName = cursor.getString(cursor.getColumnIndex(AccountColums.TYPE_NAME));
                int imageId = cursor.getInt(cursor.getColumnIndex(AccountColums.SELECTED_IMAGE));
                String comment = cursor.getString(cursor.getColumnIndex(AccountColums.COMMENT));
                float money = cursor.getFloat(cursor.getColumnIndex(AccountColums.MONEY));
                String time = cursor.getString(cursor.getColumnIndex(AccountColums.TIME));
                int yearTemp = cursor.getInt(cursor.getColumnIndex(AccountColums.YEAR));
                int monthTemp = cursor.getInt(cursor.getColumnIndex(AccountColums.MONTH));
                int dayTemp = cursor.getInt(cursor.getColumnIndex(AccountColums.DAY));
                int type = cursor.getInt(cursor.getColumnIndex(AccountColums.TYPE));
                AccountRecord accountRecord = new AccountRecord(id,typeName,imageId,comment,money,time,yearTemp,monthTemp
                        ,dayTemp,type);
                list.add(accountRecord);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 获取某年的支出或收入的总金额
     * @param kind 0~支出，1~收入
     * @return 总金额
     */
    public float getYearMoneyByKind(int year, int kind) {
        float money = 0;
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery("select sum(money) from " + ACCOUNT_TABLE_NAME + " where "
                            + AccountColums.YEAR + " = " + year + " and "
                            + AccountColums.TYPE + " = " + kind,
                    null);
            if (cursor.moveToFirst()) {
                money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return money;
    }

    public float getTotalMoneyByKind(int kind) {
        float money = 0;
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery("select sum(money) from " + ACCOUNT_TABLE_NAME + " where " + AccountColums.TYPE + " = " + kind,
                    null);
            if (cursor.moveToFirst()) {
                money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return money;
    }

    public boolean deleteById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        int result = db.delete(ACCOUNT_TABLE_NAME, AccountColums.ID + " = ?", new String[]{String.valueOf(id)});
        if (result == 0) {
            return false;
        }
        return true;
    }

    public List<AccountRecord> getListByComment(String msg) {
        List<AccountRecord> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor =
                    db.rawQuery("select * from " + ACCOUNT_TABLE_NAME + " where " + AccountColums.COMMENT + " like '%" + msg + "%'",
                            null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(AccountColums.ID));
                String typeName = cursor.getString(cursor.getColumnIndex(AccountColums.TYPE_NAME));
                int imageId = cursor.getInt(cursor.getColumnIndex(AccountColums.SELECTED_IMAGE));
                String comment = cursor.getString(cursor.getColumnIndex(AccountColums.COMMENT));
                float money = cursor.getFloat(cursor.getColumnIndex(AccountColums.MONEY));
                String time = cursor.getString(cursor.getColumnIndex(AccountColums.TIME));
                int yearTemp = cursor.getInt(cursor.getColumnIndex(AccountColums.YEAR));
                int monthTemp = cursor.getInt(cursor.getColumnIndex(AccountColums.MONTH));
                int dayTemp = cursor.getInt(cursor.getColumnIndex(AccountColums.DAY));
                int type = cursor.getInt(cursor.getColumnIndex(AccountColums.TYPE));
                AccountRecord accountRecord = new AccountRecord(id,typeName,imageId,comment,money,time,yearTemp,monthTemp
                        ,dayTemp,type);
                list.add(accountRecord);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.d(TAG, "list:" + list.toArray().toString());
        return list;
    }
}