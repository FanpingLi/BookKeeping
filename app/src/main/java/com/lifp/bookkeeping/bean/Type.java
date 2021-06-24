package com.lifp.bookkeeping.bean;

/**
 * 支出或收入类型
 */
public class Type {
    private int mId;
    private String mTypeName;
    private int mImageSelected;
    // 0-收入 1-支出
    private int mKind;

    public Type() {
    }

    public Type(int id, String typeName, int imageSelected, int kind) {
        mId = id;
        mTypeName = typeName;
        mImageSelected = imageSelected;
        mKind = kind;
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

    public int getImageSelected() {
        return mImageSelected;
    }

    public void setImageSelected(int imageSelected) {
        mImageSelected = imageSelected;
    }

    public int getKind() {
        return mKind;
    }

    public void setKind(int kind) {
        mKind = kind;
    }

    @Override
    public String toString() {
        return "Type{"
                + "mId=" + mId
                + ", mTypeName='" + mTypeName + '\''
                + ", mImageSelected=" + mImageSelected
                + ", mKind=" + mKind + '}';
    }
}