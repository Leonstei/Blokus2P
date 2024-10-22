package com.example.blokus2p.ui.bockusScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.layout



@Composable
fun ZoomableBlockusBoard() {
    var scale by remember { mutableStateOf(1f) }  // Variable zum Speichern des Zoomfaktors
    val gridSize = 14
    val cellSize = 40.dp

    var gridColors = remember {
        mutableStateListOf(*Array(gridSize * gridSize) { Color.LightGray })
    }

    // Box, die Zoom und Scroll unterstützt
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    scale *= zoom  // Berechne den neuen Zoomfaktor
                    scale = scale.coerceIn(0.5f, 3f)  // Begrenze den Zoom (z.B. zwischen 0.5x und 3x)
                }
            }
            .verticalScroll(rememberScrollState())  // Ermöglicht vertikales Scrollen
            .horizontalScroll(rememberScrollState())  // Ermöglicht horizontales Scrollen
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .layout { measurable, constraints ->
                    // Anpassung der Layoutgröße basierend auf dem Zoomfaktor
                    val placeable = measurable.measure(
                        constraints.copy(
                            maxWidth = (constraints.maxWidth * scale).toInt(),
                            maxHeight = (constraints.maxHeight * scale).toInt()
                        )
                    )
                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(0, 0)
                    }
                }
        ) {
            for (row in 0 until gridSize) {
                Row {
                    for (col in 0 until gridSize) {
                        val index = row * gridSize + col
                        Box(
                            modifier = Modifier
                                .size(cellSize * scale)  // Passe die Zellen an den Zoom an
                                .background(gridColors[index])
                                .clickable {
                                    gridColors[index] = Color.Blue
                                }
                                .padding(1.dp)
                        )
                    }
                }
            }
        }
    }
}
