package com.example.arapp.application.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

object KoinInitializer {

    private var isInitialized = false

    fun init(context: Context) {
        if (!isInitialized) {
            startKoin {
                printLogger()
                androidContext(context)
                modules(ModuleProvider.modules)
            }
        }
        isInitialized = true
    }
}