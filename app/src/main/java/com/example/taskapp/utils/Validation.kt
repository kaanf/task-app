package com.example.taskapp.utils

import android.content.Context
import android.net.Uri
import android.util.Patterns
import com.example.taskapp.utils.Extensions.showToast

object Validation {

    private fun isNullOrEmpty(input: String?): Boolean = input == null || input.isEmpty()

    fun validateData(
        context: Context,
        email: String?,
        password: String?,
        conf_password: String?
    ): Boolean {
        return isEmail(context, email) && isPassword(context, password) && isMatch(
            context,
            password,
            conf_password
        )
    }

    fun isEmail(context: Context, email: String?): Boolean {
        when {
            isNullOrEmpty(email) -> showToast(context, "Please enter Email first.")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> showToast(
                context,
                "Please enter a valid Email address."
            )
            else -> return true
        }
        return false
    }

    fun isPassword(context: Context, password: String?): Boolean {
        when {
            isNullOrEmpty(password) -> showToast(context, "Please enter password first.")
            password!!.length < 6 -> showToast(
                context,
                "Password length should not be less than 6 characters"
            )
            password.length > 30 -> showToast(
                context,
                "Password length should not be greater than 30 characters"
            )
            else -> return true
        }
        return false
    }

    private fun isMatch(context: Context, password: String?, conf_password: String?): Boolean {
        when {
            isNullOrEmpty(conf_password) -> showToast(context, "Please confirm password first.")
            password != conf_password -> showToast(context, "Passwords are not same.")
            else -> return true
        }
        return false
    }

    fun isNullInput(
        context: Context,
        name: String,
        category: String,
        price: String,
        description: String
    ): Boolean {
        when {
            isNullOrEmpty(name) -> showToast(context, "Please enter a product name.")
            isNullOrEmpty(category) -> showToast(context, "Please enter a product category.")
            isNullOrEmpty(price) -> showToast(context, "Please enter product price.")
            isNullOrEmpty(description) -> showToast(context, "Please enter product description.")
            else -> return true
        }
        return false
    }

}