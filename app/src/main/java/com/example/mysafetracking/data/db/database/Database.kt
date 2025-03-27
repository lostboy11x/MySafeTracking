package com.example.mysafetracking.data.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mysafetracking.data.db.dao.ChildDao
import com.example.mysafetracking.data.db.dao.LocationDao
import com.example.mysafetracking.data.db.dao.TutorDao
import com.example.mysafetracking.data.db.entities.ChildEntity
import com.example.mysafetracking.data.db.entities.Converters
import com.example.mysafetracking.data.db.entities.LocationEntity
import com.example.mysafetracking.data.db.entities.TutorEntity

@Database(entities = [TutorEntity::class, ChildEntity::class, LocationEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun childDao(): ChildDao
    abstract fun locationDao(): LocationDao
    abstract fun tutorDao(): TutorDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mysafetracking_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}