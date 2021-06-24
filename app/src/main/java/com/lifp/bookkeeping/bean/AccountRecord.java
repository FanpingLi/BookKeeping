package com.lifp.bookkeeping.bean;

public class AccountRecord {

    private int mId;

    private String mTypeName;

    private int mSlectedImage;

    private String mComment;

    private float mMoney;

    private String mTime;

    private int mYear;

    private int mMonth;

    private int mDay;

    private int mType;

    public AccountRecord() {
    }

    public AccountRecord(int id, String typeName, int slectedImage, String comment, float money, String time,
                         int year, int month, int day, int type) {
        mId = id;
        mTypeName = typeName;
        mSlectedImage = slectedImage;
        mComment = comment;
        mMoney = money;
        mTime = time;
        mYear = year;
        mMonth = month;
        mDay = day;
        mType = type;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTypeName() {
        return mTypeName;
    }

    public void setTypeName(String typeName) {
        mTypeName = typeName;
    }

    public int getSlectedImage() {
        return mSlectedImage;
    }

    public void setSlectedImage(int slectedImage) {
        mSlectedImage = slectedImage;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public float getMoney() {
        return mMoney;
    }

    public void setMoney(float money) {
        mMoney = money;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        mMonth = month;
    }

    public int getDay() {
        return mDay;
    }

    public void setDay(int day) {
        mDay = day;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }
}
