package com.example.taskapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.R
import com.example.taskapp.model.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_item.view.*

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val products = mutableListOf<Product>()
    private val onItemClickLiveData = MutableLiveData<Product>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view, onItemClickLiveData)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) = holder.setItem(products[position])

    override fun getItemCount(): Int = products.size

    fun onFeedUpdate(products: List<Product>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }

    fun onPostSorting(products: List<Product>) {
        products.sortedBy {
            it.name
        }
        notifyDataSetChanged()
    }

    fun onPostItemClick(): LiveData<Product> = onItemClickLiveData

    class ProductViewHolder(private val view: View, private val onItemClickLiveData: MutableLiveData<Product>) : RecyclerView.ViewHolder(view) {

        private lateinit var product: Product
        private lateinit var imageUri: String

        init {
            view.setOnClickListener { onItemClickLiveData.postValue(product) }
        }

        @SuppressLint("SetTextI18n")
        fun setItem(product: Product) {
            this.product = product
            imageUri = product.image
            with(view) {
                product_details_name.text = product.name
                product_details_price.text = "$" + product.price
                product_details_category.text = product.category
                product_details_timestamp.text = product.timestamp
                Picasso.get().load(imageUri).into(view.image)
            }
        }
    }
}