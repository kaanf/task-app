package com.example.taskapp.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentProductBinding
import com.example.taskapp.model.Product
import com.example.taskapp.repository.ProductRepository
import com.example.taskapp.ui.adapter.ProductAdapter
import com.example.taskapp.ui.base.BaseFragment
import com.example.taskapp.vm.ProductViewModel

class ProductFragment: BaseFragment<ProductViewModel, FragmentProductBinding, ProductRepository>() {

    private val productAdapter = ProductAdapter()
    private val productRepository = ProductRepository()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.homeProgressBar.visibility = View.GONE
            binding.homeProgressText.visibility = View.GONE
            binding.productsFeed.visibility = View.VISIBLE
        }, 3000)
        initialize()
        binding.addProduct.setOnClickListener {
            findNavController().navigate(R.id.action_productFragment_to_addProductFragment)
        }
    }

    private fun listenForPostsUpdates() {
        productRepository.onProductsValuesChange()
            .observe(viewLifecycleOwner, Observer(::onPostsUpdate))
    }

    override fun onStart() {
        super.onStart()
        listenForPostsUpdates()
    }

    override fun onStop() {
        super.onStop()
        productRepository.removeProductsValuesChangesListener()
    }

    private fun initialize() {
        productAdapter.onPostItemClick()
            .observe(viewLifecycleOwner, Observer(::onPostItemClick))
    }

    private fun onPostsUpdate(products: List<Product>) {
        productAdapter.onFeedUpdate(products)
    }

    private fun onPostItemClick(product: Product) {
        val action = ProductFragmentDirections.actionProductFragmentToProductDetailsFragment(product)
        findNavController().navigate(action)
    }

    private fun initializeRecyclerView() {
        binding.productsFeed.layoutManager = LinearLayoutManager(requireContext())
        binding.productsFeed.setHasFixedSize(true)
        binding.productsFeed.adapter = productAdapter
    }

    override fun getViewModel(): Class<ProductViewModel> = ProductViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProductBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = ProductRepository()

}