package com.example.arapp.application.di

import org.koin.core.module.Module

object ModuleProvider {
    val modules: List<Module>
        get() = ArrayList<Module>().apply {
            addAll(AppModule.modules)
        }
}