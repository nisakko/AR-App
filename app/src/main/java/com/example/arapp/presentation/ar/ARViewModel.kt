package com.example.arapp.presentation.ar

import androidx.lifecycle.ViewModel
import com.example.arapp.presentation.ar.model.ARModel
import com.example.arapp.presentation.ar.model.ModelTexture
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class ARViewModel : ViewModel() {

    val models = ARModel.values().toList()
    val textures = ModelTexture.values().toList()
    private val _selectedModel = MutableStateFlow(models.first())
    val selectedModel = _selectedModel.asStateFlow()

    private val _texture = MutableStateFlow(ModelTexture.None)
    val texture = _texture.asStateFlow()

    fun selectModel(index: Int) {
        _selectedModel.value = models[index]
    }

    fun selectTexture(index: Int) {
        _texture.value = textures[index]
    }
}