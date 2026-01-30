package com.jhovahn.habittracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Habit::class], version = 3)
abstract class HabitDatabase : RoomDatabase()  {
    abstract fun habitDao(): HabitDao

    companion object {
        @Volatile
        private var INSTANCE: HabitDatabase? = null

        fun getDatabase(context: Context): HabitDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                                context.applicationContext,
                                HabitDatabase::class.java,
                                "habit_database"
                            ).addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

val MIGRATION_1_2 = object: Migration(1,2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE habit_table ADD COLUMN timerDuration INTEGER NOT NULL DEFAULT 1500000"
        )
        db.execSQL(
            "ALTER TABLE habit_table ADD COLUMN timerEnd INTEGER"
        )
        db.execSQL(
            "ALTER TABLE habit_table ADD COLUMN streak INTEGER NOT NULL DEFAULT 0"
        )
        db.execSQL(
            "ALTER TABLE habit_table ADD COLUMN lastCompleted INTEGER"
        )
    }
}

val MIGRATION_2_3 = object: Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE habit_table ADD COLUMN weeklyGoal INTEGER NOT NULL DEFAULT 5"
        )
        db.execSQL(
            "ALTER TABLE habit_table ADD COLUMN weeklyCompleted INTEGER NOT NULL DEFAULT 0"
        )
        db.execSQL(
            "ALTER TABLE habit_table ADD COLUMN weekStartTimestamp INTEGER NOT NULL DEFAULT 0"
        )
    }
}