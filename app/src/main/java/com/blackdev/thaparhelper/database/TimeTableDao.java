package com.blackdev.thaparhelper.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TimeTableDao {

    @Insert(onConflict = REPLACE)
    void insert(TimeTableData data);

    @Delete
    void reset(List<TimeTableData> data);

    @Delete
    void deleteEntry(TimeTableData data);

    @Query("SELECT * FROM timeTable where mSubjectName = :subject")
    List<TimeTableData> getSubjectWeekTimeTable(String subject);

    @Query("SELECT * FROM timeTable where mSubjectName = :subject and mDay = :mDay")
    List<TimeTableData> getDaySubjectTimeTable(String subject, int mDay);

    @Query("SELECT * FROM timeTable where mDay = :mDay")
    List<TimeTableData> getDaysTimeTable( String mDay);

    @Query("Select * from timeTable")
    public List<TimeTableData> getAll();


}
