package com.example.arapp.application.di

import android.content.ContentResolver
import com.example.arapp.presentation.ar.ARSetupViewModel
import com.example.arapp.presentation.ar.ARViewModel
import com.example.arapp.util.ARUtil
import com.example.arapp.util.PermissionUtil
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.dsl.single

object AppModule {

    private val appModules = module {
        single<ContentResolver> { androidContext().contentResolver }
    }

    private val utilModules = module {
        single<PermissionUtil>()
        single<ARUtil>()
    }

    private val viewModelModules = module {
        viewModel<ARSetupViewModel>()
        viewModel<ARViewModel>()
    }

    val modules: List<Module> = listOf(
        appModules, viewModelModules, utilModules
    )
}