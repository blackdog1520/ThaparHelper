package com.blackdev.thaparhelper.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities =  {ChatData.class, AllUsersData.class, TimeTableData.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase database;
    private static String DATABASE_NAME = "Thapar_database";

    public synchronized static AppDatabase getInstance(Context context) {
        if(database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
    public  abstract ChatDataDao chatDataDao();

    public  abstract TimeTableDao timeTableDao();


}
