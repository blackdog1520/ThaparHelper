package com.blackdev.thaparhelper.database;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "timeTable")
public class TimeTableData implements Serializable {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mID")
    private int id;

    @NonNull
    @ColumnInfo(name = "mSubjectName")
    String mSubjectName;

    @NonNull
    @ColumnInfo(name = "mDay")
    int mDay;

    @NonNull
    @ColumnInfo(name = "mHr")
    int mHH;

    @NonNull
    @ColumnInfo(name = "mMin")
    int mMM;


    @ColumnInfo(name = "mType")
    int classType;

    public String getmSubjectName() {
        return mSubjectName;
    }

    public void setmSubjectName(String mSubjectName) {
        this.mSubjectName = mSubjectName;
    }


    public int getClassType() {
        return classType;
    }

    public void setClassType(int classType) {
        this.classType = classType;
    }

    @Ignore
    public TimeTableData() {
    }

    public int getmDay() {
        return mDay;
    }

    public void setmDay(int mDay) {
        this.mDay = mDay;
    }

    public int getmHH() {
        return mHH;
    }

    public void setmHH(int mHH) {
        this.mHH = mHH;
    }

    public int getmMM() {
        return mMM;
    }

    public void setmMM(int mMM) {
        this.mMM = mMM;
    }

    public TimeTableData(@NonNull String mSubjectName, int mDay, int mHH, int mMM, int classType) {
        this.mSubjectName = mSubjectName;
        this.mDay = mDay;
        this.mHH = mHH;
        this.mMM = mMM;
        this.classType = classType;
    }
}
