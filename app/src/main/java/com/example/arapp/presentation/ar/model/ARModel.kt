package com.example.arapp.presentation.ar.model

enum class ARModel(
    val title: String,
    val path: String
) {
    Tiger(
        title = "Tiger",
        path = "models/tiger.glb"
    ),
    Panda(
      title = "Panda",
      path = "models/panda.glb"
    ),
    Cube(
        title = "Cube",
        path = "models/cube.glb"
    )
}