package com.example.taskapp.utils


import android.app.Activity
import com.example.taskapp.ui.HomeActivity

class Router {

    fun startHomeScreen(activity: Activity) {
        val intent = HomeActivity.createIntent(activity)
        activity.startActivity(intent)
    }

}