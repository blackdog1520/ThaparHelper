package com.blackdev.thaparhelper.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "AllContacts")
public class AllUsersData {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    int Id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
