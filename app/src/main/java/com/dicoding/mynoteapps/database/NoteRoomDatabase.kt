package com.dicoding.mynoteapps.database

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Note::class], version = 2)
abstract class NoteRoomDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): NoteRoomDatabase {
            if (INSTANCE == null) {
                synchronized(NoteRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        NoteRoomDatabase::class.java, "note_database")
                        .addMigrations(MIGRATION_1_2)
                        .build()
                }
            }
            return INSTANCE as NoteRoomDatabase
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            @SuppressLint("Range")
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the note_table if it doesn't exist
                database.execSQL("CREATE TABLE IF NOT EXISTS `note_table` " +
                        "(`id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "`title` TEXT NOT NULL, " +
                        "`description` TEXT NOT NULL, " +
                        "`date` TEXT NOT NULL, " +
                        "`completed` INTEGER NOT NULL DEFAULT 0)")

                // Check if the `completed` column doesn't exist before adding it
                val cursor = database.query("PRAGMA table_info(`note_table`)")
                var columnExists = false
                while (cursor.moveToNext()) {
                    val columnName = cursor.getString(cursor.getColumnIndex("name"))
                    if ("completed" == columnName) {
                        columnExists = true
                        break
                    }
                }
                cursor.close()

                if (!columnExists) {
                    // Add a boolean column for completed state
                    database.execSQL("ALTER TABLE `note_table` ADD COLUMN `completed` INTEGER NOT NULL DEFAULT 0")
                }
            }
        }


    }
}
