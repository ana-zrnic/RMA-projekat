package com.example.androidview.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    fun insert(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    fun login(email: String?, password: String?): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email")
    fun getUserByEmail(email: String?): UserEntity?

    @Query("SELECT * FROM users WHERE user_name = :userName")
    fun getUserByUserName(userName: String?): UserEntity?

    @Query("SELECT * FROM users WHERE user_name = :username OR email = :email LIMIT 1")
    fun getUserByUsernameOrEmail(username: String, email: String): UserEntity?

    @Update
    fun update(user: UserEntity)

    @Delete
    fun delete(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Int): UserEntity?


    @get:Query("SELECT * FROM users")
    val allUsers: List<UserEntity>
}