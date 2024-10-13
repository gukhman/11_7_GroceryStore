package com.example.grocerystore

import java.io.Serializable

data class Item(val name: String, val price: String, val description: String, var image: String?) : Serializable