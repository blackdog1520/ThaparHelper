package com.blackdev.thaparhelper.database;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ChatDataDao {
    @Insert(onConflict = REPLACE)
    void insert(ChatData chatData);

    @Delete
    void reset(List<ChatData> chatData);

    @Query("SELECT * FROM chatsTable where mFROMUID = :hisUid or mTOUID = :hisUid")
    List<ChatData> getMessagesOfUser(String hisUid);


    @Query("UPDATE chatsTable SET Seen = :flag where mTOUID = :hisUID and TimeStamp = :timeStamp")
    void updateMessage(boolean flag, String hisUID, String timeStamp);

    @Query("SELECT * FROM chatsTable group by mTOUID and mFROMUID")
    List<ChatData> getChatHistory();


}
