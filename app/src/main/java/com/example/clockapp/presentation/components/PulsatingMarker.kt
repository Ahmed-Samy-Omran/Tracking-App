package com.example.clockapp.presentation.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
//
//@Composable
//fun PulsatingMarker(position: LatLng) {
//    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
//
//    val radius by infiniteTransition.animateFloat(
//        initialValue = 20f,
//        targetValue = 40f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(1000, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        ), label = "radius"
//    )
//
//    val color by infiniteTransition.animateColor(
//        initialValue = Color(0x804285F4),
//        targetValue = Color(0x004285F4),
//        animationSpec = infiniteRepeatable(
//            animation = tween(1000, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        ), label = "color"
//    )
//
//    Circle(
//        center = position,
//        radius = radius.toDouble(),
//        strokeWidth = 0f,
//        fillColor = color
//    )
//}
//

/**
 * Static circle marker for current location (no animation)
 */
@Composable
fun CurrentLocationMarker(position: LatLng) {
    // Outer circle (border)
    Circle(
        center = position,
        radius = 18.0,
        strokeWidth = 3f,
        strokeColor = Color.White,
        fillColor = Color.Transparent
    )

    // Inner circle (filled)
    Circle(
        center = position,
        radius = 12.0,
        strokeWidth = 0f,
        fillColor = Color.Black
    )
}

/**
 * Animated pulsating circle for target location
 */
@Composable
fun TargetLocationMarker(position: LatLng) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val radius by infiniteTransition.animateFloat(
        initialValue = 25f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "radius"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "alpha"
    )

    // Pulsating outer circle
    Circle(
        center = position,
        radius = radius.toDouble(),
        strokeWidth = 0f,
        fillColor = Color.Black.copy(alpha = alpha)
    )

    // Static outer border
    Circle(
        center = position,
        radius = 18.0,
        strokeWidth = 3f,
        strokeColor = Color.Black,
        fillColor = Color.Transparent
    )

    // Static inner circle
    Circle(
        center = position,
        radius = 12.0,
        strokeWidth = 0f,
        fillColor = Color.White
    )
}

/**
 * Legacy component - kept for backward compatibility
 * Use TargetLocationMarker instead
 */
@Composable
fun PulsatingMarker(position: LatLng) {
    TargetLocationMarker(position)
}

