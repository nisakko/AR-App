package com.example.arapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.arapp.R
import com.google.ar.core.ArCoreApk

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // to have a cached result call checkAvailability immediately, ignore the return value in here
        ArCoreApk.getInstance().checkAvailability(this)
    }
}