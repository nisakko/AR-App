package com.example.arapp.presentation.ar.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arapp.presentation.ar.model.ARModel
import com.example.arapp.presentation.ar.model.ModelTexture
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState

@ExperimentalPagerApi
@Composable
internal fun HorizontalTexturePager(
    models: List<ModelTexture>,
    selectedItemIndex: (index: Int) -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = models.size,
        infiniteLoop = true
    )

    PagerColumn(pagerState = pagerState, title = "Texture", selectedItemIndex = selectedItemIndex) {
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = pagerState,
            itemSpacing = 16.dp
        ) { page ->
            PagerContent(
                isSelected = page == pagerState.currentPage,
                title = models[page].title
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
internal fun HorizontalModelPager(
    models: List<ARModel>,
    selectedItemIndex: (index: Int) -> Unit
) {
    val pagerState = rememberPagerState(
        pageCount = models.size,
        infiniteLoop = true
    )

    PagerColumn(pagerState = pagerState, title = "Model", selectedItemIndex = selectedItemIndex) {
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = pagerState,
            itemSpacing = 16.dp
        ) { page ->
            PagerContent(
                isSelected = page == pagerState.currentPage,
                title = models[page].title
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
private fun PagerColumn(
    pagerState: PagerState,
    title: String,
    selectedItemIndex: (index: Int) -> Unit,
    content: @Composable () -> Unit
) {
    selectedItemIndex(pagerState.currentPage)

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = title,
            color = Color.Yellow
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun PagerContent(
    title: String,
    isSelected: Boolean = false
) {
    Box(
        modifier = Modifier
            .size(width = 86.dp, height = 48.dp)
            .background(
                color = if (isSelected) Color.Green else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .border(width = 2.dp, shape = RoundedCornerShape(20.dp), color = Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = Color.White,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}