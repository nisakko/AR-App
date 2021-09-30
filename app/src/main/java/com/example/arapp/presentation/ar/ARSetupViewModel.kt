package com.example.arapp.presentation.ar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arapp.util.ARUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ARSetupViewModel(
    private val arUtil: ARUtil
) : ViewModel() {

    private val _isARSupported = MutableStateFlow<Boolean>(false)
    val isARSupported = _isARSupported.asStateFlow()

    init {
        viewModelScope.launch {
            _isARSupported.value = arUtil.isARSupported()
        }
    }
}