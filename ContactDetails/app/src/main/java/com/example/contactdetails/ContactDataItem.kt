package com.example.contactdetails


import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Contacts")
data class ContactDataItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "name")
    var contactname: String?,
    @ColumnInfo(name = "number")
    var contactnumber:String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var image: Bitmap?
)