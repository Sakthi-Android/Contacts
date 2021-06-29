package com.example.contactdetails


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = arrayOf(ContactDataItem::class), version = 1)
@TypeConverters
abstract class ContactRoomDB : RoomDatabase() {
    abstract fun dataDAO(): DataDAO

    companion object {
        private var INSTANCE: ContactRoomDB? = null
        fun getDatabase(context: Context): ContactRoomDB? {
            if (INSTANCE == null) {

                synchronized(ContactRoomDB::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        ContactRoomDB::class.java,"contactdb"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}