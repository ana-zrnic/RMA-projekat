package com.example.androidview.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.lang.RuntimeException


@Database(entities = [UserEntity::class, PollEntity::class, OptionEntity::class, ResponseEntity::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun pollDao(): PollDao
    abstract fun optionDao(): OptionDao
    abstract fun responseDao(): ResponseDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                try{
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    ).fallbackToDestructiveMigration() // Handle migration appropriately.
                        .build()
                    INSTANCE = instance
                    instance
                }catch (e: Exception){
                    throw RuntimeException("Database creation failed", e)
                }
            }
        }
    }
}