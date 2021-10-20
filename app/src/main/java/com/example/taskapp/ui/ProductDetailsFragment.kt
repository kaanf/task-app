package com.example.taskapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentProductDetailsBinding
import com.example.taskapp.model.Product
import com.example.taskapp.repository.ProductRepository
import com.example.taskapp.ui.base.BaseFragment
import com.example.taskapp.utils.Validation
import com.example.taskapp.vm.ProductDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso

class ProductDetailsFragment :
    BaseFragment<ProductDetailsViewModel, FragmentProductDetailsBinding, ProductRepository>() {

    private val args by navArgs<ProductDetailsFragmentArgs>()
    private val productRepository by lazy { ProductRepository() }
    private lateinit var product: Product

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun buttonClickInitializers() {
        binding.apply {
            update.setOnClickListener {
                toggleBottomSheetDialog()
            }
            delete.setOnClickListener {
                productRepository.deleteProduct(product.key)
                findNavController().navigate(R.id.action_productDetailsFragment_to_productFragment)
            }
            icBackArrow.setOnClickListener {
                findNavController().navigate(R.id.action_productDetailsFragment_to_productFragment)
            }
        }
    }

    override fun getViewModel(): Class<ProductDetailsViewModel> =
        ProductDetailsViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProductDetailsBinding =
        FragmentProductDetailsBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): ProductRepository = ProductRepository()

    @SuppressLint("SetTextI18n")
    fun initialize() {
        buttonClickInitializers()
        product = args.productArgs
        binding.apply {
            productDetailsName.text = product.name
            productDetailsPrice.text = "$" + product.price
            binding.productDetailsDescription.text = product.description
            productDetailsCategory.text = product.category
            Picasso.get().load(product.image).into(productImageDetails)
        }
    }

    private fun toggleBottomSheetDialog() {
        val view = layoutInflater.inflate(R.layout.fragment_bottom_sheet, null)
        val updateButton: AppCompatButton = view.findViewById(R.id.button_update)
        val nameUpdate: EditText = view.findViewById(R.id.name_update)
        val categoryUpdate: EditText = view.findViewById(R.id.category_update)
        val priceUpdate: EditText = view.findViewById(R.id.price_update)
        val descriptionUpdate: EditText = view.findViewById(R.id.description_update)
        val dialog = activity?.let { BottomSheetDialog(it) }
        dialog?.setContentView(view)
        dialog?.show()
        updateButton.setOnClickListener {
            if (Validation.isNullInput(
                    requireContext(),
                    nameUpdate.text.toString(),
                    categoryUpdate.text.toString(),
                    priceUpdate.text.toString(),
                    descriptionUpdate.text.toString()
                )
            ) {
                dialog?.hide()
                binding.apply {
                    product.name = nameUpdate.text.toString()
                    product.description = descriptionUpdate.text.toString()
                    product.category = categoryUpdate.text.toString()
                    product.price = priceUpdate.text.toString()
                    productRepository.updateProduct(product.key, product)
                }

                findNavController().navigate(R.id.action_productDetailsFragment_to_productFragment)
            } else {
                dialog?.hide()
            }
        }
    }

}