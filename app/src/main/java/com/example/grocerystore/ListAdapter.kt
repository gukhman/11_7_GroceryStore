package com.example.grocerystore

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ListAdapter(context: Context, itemList: MutableList<Item>) :
    ArrayAdapter<Item>(context, R.layout.list_item, itemList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val item = getItem(position)
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val listImageViewIV = view?.findViewById<ImageView>(R.id.productPhoto)
        val listNameET = view?.findViewById<TextView>(R.id.listNameTV)
        val listPriceET = view?.findViewById<TextView>(R.id.listPriceTV)

        listImageViewIV?.setImageURI(Uri.parse(item?.image))
        listNameET?.text = item?.name
        listPriceET?.text = item?.price.toString()
        return view!!
    }
}