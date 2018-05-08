package com.joegruff.readabook

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val file = File("/sdcard/ebooks");

        val array2 = file.listFiles()
        val array = Array<String>(array2.size, { i -> array2[i].absolutePath })

        val myAdapter = ArrayAdapter(this, R.layout.filename_text_view, array)
        list_view.adapter = myAdapter
        list_view.setOnItemClickListener { parent, view, position, id ->
            Log.d("clicked", position.toString())
            Log.d("bout to log", parent.getItemAtPosition(position).toString())
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

class GenericFileProvider : FileProvider()