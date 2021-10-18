package com.example.taskapp.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentAddProductBinding
import com.example.taskapp.repository.ProductRepository
import com.example.taskapp.ui.base.BaseFragment
import com.example.taskapp.utils.Extensions
import com.example.taskapp.utils.Validation
import com.example.taskapp.vm.AddProductViewModel

class AddProductFragment :
    BaseFragment<AddProductViewModel, FragmentAddProductBinding, ProductRepository>() {

    private var imageData: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonClickInitializers()
    }

    private fun buttonClickInitializers() {
        binding.apply {
            addProductButton.setOnClickListener {
                if (imageData == null) {
                    Extensions.showToast(requireContext(), "Please upload an image.")
                } else {
                    val productName = addProductName.text.toString()
                    val productCategory = addProductCategory.text.toString()
                    val productPrice = addProductPrice.text.toString()
                    val productDescription = addProductDescription.text.toString()
                    if (Validation.isNullInput(
                            requireContext(),
                            productName,
                            productCategory,
                            productPrice,
                            productDescription
                        )
                    ) {
                        viewModel.addProduct(productName,
                            productDescription,
                            productPrice,
                            productCategory,
                            imageData.toString(),
                            {
                                findNavController().navigate(R.id.action_addProductFragment_to_productFragment)
                            },
                            { Extensions.showToast(requireContext(), "Failed, sorry.") })

                    } else {
                        Extensions.showToast(requireContext(), "Empty product.")
                    }
                }

            }
            icBackArrow.setOnClickListener {
                findNavController().navigate(R.id.action_addProductFragment_to_productFragment)
            }
            addProductImage.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, 100)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageData = data?.data!!
            binding.addImage.setImageURI(imageData)
        }
    }

    override fun getViewModel(): Class<AddProductViewModel> = AddProductViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAddProductBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = ProductRepository()
}