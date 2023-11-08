package com.example.filmus.domain.database.reviews

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserReviewEntity::class], version = 1, exportSchema = false)
abstract class UserReviewDatabase : RoomDatabase() {
    abstract fun userReviewDao(): UserReviewDao

    companion object {
        @Volatile
        private var INSTANCE: UserReviewDatabase? = null

        fun getDatabase(context: Context): UserReviewDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserReviewDatabase::class.java,
                    "user_review_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}