import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.grocerystore.R

data class ProductAdapter(context: Context, private val products: List<Product>) :
    ArrayAdapter<Product>(context, 0, products) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R., parent, false)
        val product = products[position]

        val productName = view.findViewById<TextView>(R.id.productName)
        val productPrice = view.findViewById<TextView>(R.id.productPrice)
        val productImage = view.findViewById<ImageView>(R.id.productImage)

        productName.text = product.name
        productPrice.text = product.price
        product.imageUri?.let {
            productImage.setImageURI(it)
        }

        return view
    }
}