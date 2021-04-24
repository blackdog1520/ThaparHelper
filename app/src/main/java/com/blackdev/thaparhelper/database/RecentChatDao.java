package com.blackdev.thaparhelper.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface RecentChatDao {
    @Insert(onConflict = REPLACE)
    void insert(RecentChatData data);

    @Delete
    void deleteEntry(RecentChatData data);

    @Query("Select * from recentChat")
    public List<RecentChatData> getAll();

}
