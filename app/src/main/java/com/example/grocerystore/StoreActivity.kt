package com.example.grocerystore

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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

class StoreActivity : AppCompatActivity() {

    private val galleryRequest = 302
    private val itemDescriptionRequest = 15
    private var items: MutableList<Item> = mutableListOf()
    private var photoUri: Uri? = null

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var productName: EditText
    private lateinit var productPrice: EditText
    private lateinit var productDescription: EditText
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
        productDescription = findViewById(R.id.descriptionET)
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
            val itemDescription = productDescription.text.toString()
            val itemImage = photoUri.toString()
            val item = Item(itemName, itemPrice, itemDescription, itemImage)
            items.add(item)
            photoUri = null

            val listAdapter = ListAdapter(this, items)
            listView.adapter = listAdapter
            listAdapter.notifyDataSetChanged()
            productName.text.clear()
            productPrice.text.clear()
            productDescription.text.clear()
            productImage.setImageResource(R.drawable.photo_default)
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val product = items[position]
            val intent = Intent(this, ItemDescription::class.java)
            intent.putExtra("product", product)
            intent.putExtra("position", position)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Добавляем флаг для разрешения
            startActivityForResult(intent, itemDescriptionRequest)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == galleryRequest) {
            if (resultCode == RESULT_OK) {
                photoUri = data?.data
                productImage.setImageURI(photoUri)
            }
        } else if (requestCode == itemDescriptionRequest) {
            if (resultCode == RESULT_OK && data != null) {
                val updatedItem = data.getSerializableExtra("updatedProduct") as? Item
                val position = data.getIntExtra("position", -1)
                if (updatedItem != null && position != -1) {
                    items[position] = updatedItem
                    (listView.adapter as ListAdapter).notifyDataSetChanged()
                } else {
                    Log.e("StoreActivity", "Invalid item data or position")
                }
            }
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
