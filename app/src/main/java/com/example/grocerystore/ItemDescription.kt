package com.example.grocerystore

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ItemDescription : AppCompatActivity() {

    private val galleryRequest = 15
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var productName: TextView
    private lateinit var productPrice: TextView
    private lateinit var productDescription: TextView
    private lateinit var productImage: ImageView
    private var currentProduct: Item? = null
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_item_description)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toolbar = findViewById(R.id.toolbarDesc)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        productName = findViewById(R.id.listNameTV)
        productPrice = findViewById(R.id.listPriceTV)
        productDescription = findViewById(R.id.listDescriptionTV)
        productImage = findViewById(R.id.productPhoto)

        currentProduct = intent.getSerializableExtra("product") as? Item
        if (currentProduct != null) {
            productName.text = currentProduct!!.name
            productPrice.text = currentProduct!!.price
            productDescription.text = currentProduct!!.description
            productImage.setImageURI(Uri.parse(currentProduct!!.image))
        }

        productImage.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, galleryRequest)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_exit -> {
                finishAffinity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.exit, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == galleryRequest) {
            if (resultCode == RESULT_OK) {
                photoUri = data?.data
                productImage.setImageURI(photoUri)
                currentProduct?.image = photoUri.toString()

                val resultIntent = Intent()
                resultIntent.putExtra("updatedProduct", currentProduct)
                resultIntent.putExtra("position", intent.getIntExtra("position", -1))
                resultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Добавляем флаг для разрешения
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}
