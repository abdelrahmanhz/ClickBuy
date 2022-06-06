package com.example.clickbuy.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.clickbuy.models.Favorite
import com.example.clickbuy.util.Constants.APP_DATABASE_NAME

@Database(entities = [Favorite::class], version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun ProductDao(): ProductDao

    companion object{
        @Volatile
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    APP_DATABASE_NAME
                )
                    .build()
            }
            return instance
        }
    }
}