package com.example.contactdetails

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query

@Dao
interface DataDAO {
    //Insert one item
    @Insert(onConflict = IGNORE)
    fun insertItem(contactDataItem: ContactDataItem?)

    //Get all items
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun allData(): List<ContactDataItem>
}