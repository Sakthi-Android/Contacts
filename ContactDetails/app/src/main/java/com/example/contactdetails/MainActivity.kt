package com.example.contactdetails

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.InputStream


class MainActivity: Activity() {

    var listcont:List<ContactDataItem>?= null
    var adapter:ContactAdapter?=null
    var recyclerView:RecyclerView?=null
    var activity:Activity?=null

    var db:ContactRoomDB?=null
    var dao:DataDAO?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_list)
        recyclerView=findViewById(R.id.list)
        var swipe:SwipeRefreshLayout=findViewById(R.id.swiperefresh)
        activity=this

        swipe.setOnRefreshListener(OnRefreshListener {
            if(swipe.isRefreshing)
            swipe.isRefreshing=false
            checkpermission()
        })
        checkpermission()

    }
    @SuppressLint("CheckResult")
    fun getDetails() {
        var photo:Bitmap?=null
        var uri:Uri?=ContactsContract.Contacts.CONTENT_URI
        var sort=ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC"
        var cursor: Cursor? =contentResolver.query(uri!!,null,null,null,sort)
        if(cursor!!.count>0){
            while (cursor.moveToNext()){
                var id: String? =cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                var name:String?=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                var phoneuri:Uri?=ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                var select:String=ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?"
                var phonecursor: Cursor? =contentResolver.query(phoneuri!!,null,select,
                    arrayOf(id),null)




                if(phonecursor!!.moveToNext()){
                    val inputStream: InputStream? =ContactsContract.Contacts.openContactPhotoInputStream(
                        contentResolver,
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,id!!.toLong())
                    )

                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream)
                    }

                    var number:String=phonecursor.getString(phonecursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                    Observable.fromCallable ({
                        db = ContactRoomDB.getDatabase(this)
                        dao = db?.dataDAO()

                        var contactlist = ContactDataItem(name, number, photo)
                        with(dao) {
                            this?.insertItem(contactlist)
                        }
                        db?.dataDAO()?.allData()
                    }
                    ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
                    {
                        Log.d("size",it?.size.toString())
                        listcont=it
                    }

//                    var contactData=ContactData(photo,name,number)
//                    listcont?.add(contactData)
                    phonecursor.close()
                    inputStream?.close()
                }
            }
            cursor.close()
        }
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.setLayoutManager(llm)
        adapter=ContactAdapter(activity!!,listcont)
        recyclerView?.setAdapter(adapter)
    }
    private fun checkpermission(){

        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS),100)

        }else{
            getDetails()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==100&&grantResults.size>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            getDetails()
        }else{
            Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show()
            checkpermission()
        }
    }

}