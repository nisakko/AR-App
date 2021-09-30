package com.example.arapp.presentation.ar.model

enum class ModelTexture(
    val title: String,
    val path: String
) {
    None(title = "None", path = ""),
    Parquet(title = "Parquet", path = "textures/parquet.jpeg"),
    Marble(title = "Marble", path = "textures/marble.png")
}