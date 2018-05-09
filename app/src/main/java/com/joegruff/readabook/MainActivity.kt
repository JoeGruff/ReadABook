package com.joegruff.readabook

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {
    //path where pdfs are stored
    val DIRECTORY_FOR_PDFS = "/sdcard/ebooks"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val file = File(DIRECTORY_FOR_PDFS);
        //get all files there then store absolute strings
        val array2 = file.listFiles()
        val array = Array<String>(array2.size, { i -> array2[i].absolutePath })
        //adapter for listview
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, array)

        list_view.adapter = myAdapter
        //selected items broadcast to pdf readers
        list_view.setOnItemClickListener { parent, view, position, id ->
            val uri: Uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".com.joegruff.readabook.provider", File(parent.getItemAtPosition(position).toString()))
            val target = Intent(Intent.ACTION_VIEW)
            target.setDataAndType(uri, "application/pdf")
            target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or FLAG_GRANT_READ_URI_PERMISSION

            val intent = Intent.createChooser(target, "Open File")
            try {
                startActivity(this, intent, null)
            } catch (e: ActivityNotFoundException) {
                // Instruct the user to install a PDF reader here, or something
            }
        }
    }

}

//needed for unique uri?
class GenericFileProvider : FileProvider()