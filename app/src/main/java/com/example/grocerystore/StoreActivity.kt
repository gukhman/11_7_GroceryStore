package com.example.grocerystore

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException

class StoreActivity : AppCompatActivity() {

    private val galleryRequest = 302
    var bitmap: Bitmap? = null
    var items: MutableList<Item> = mutableListOf()

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var productName: EditText
    private lateinit var productPrice: EditText
    private lateinit var productImage: ImageView
    private lateinit var addBTN: Button
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_store)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toolbar = findViewById(R.id.toolbarList)
        setSupportActionBar(toolbar)

        productName = findViewById(R.id.nameET)
        productPrice = findViewById(R.id.priceET)
        productImage = findViewById(R.id.photoIW)
        addBTN = findViewById(R.id.buttonAdd)
        listView = findViewById(R.id.listView)

        productImage.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, galleryRequest)
        }

        addBTN.setOnClickListener {
            val itemName = productName.text.toString()
            val itemPrice = productPrice.text.toString()
            val itemImage = bitmap
            val item = Item(itemName, itemPrice, itemImage)
            items.add(item)

            val listAdapter = ListAdapter(this, items)
            listView.adapter = listAdapter
            listAdapter.notifyDataSetChanged()
            productName.text.clear()
            productPrice.text.clear()
            productImage.setImageDrawable(null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == galleryRequest) if (resultCode === RESULT_OK) {
            val selectedImage: Uri? = data?.data
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            productImage.setImageBitmap(bitmap)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.exit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_exit) finishAffinity()
        return true
    }
}