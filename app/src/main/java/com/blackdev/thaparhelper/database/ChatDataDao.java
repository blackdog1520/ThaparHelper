package com.blackdev.thaparhelper.database;

import java.util.List;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

public interface ChatDataDao {
    @Insert(onConflict = REPLACE)
    void insert(ChatData chatData);

    @Delete
    void reset(List<ChatData> chatData);

    @Query("SELECT * FROM chatsTable where mFROMUID = :hisUid or mTOUID = :hisUid")
    void getMessagesOfUser(String hisUid);
}
