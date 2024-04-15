package com.example.androidview.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert
    void insertUser(UserEntity user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    UserEntity getUser(String username, String password);
}
