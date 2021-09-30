package com.example.arapp.application

import android.app.Application
import com.example.arapp.application.di.KoinInitializer

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        KoinInitializer.init(this)
    }
}