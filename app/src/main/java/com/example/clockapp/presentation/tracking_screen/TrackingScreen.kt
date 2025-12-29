package com.example.clockapp.presentation.tracking_screen

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.clockapp.R
import com.example.clockapp.presentation.components.CurrentLocationMarker
import com.example.clockapp.presentation.components.PulsatingMarker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun TrackingScreen(
    navHostController: NavHostController,
    viewModel: TrackViewModel = hiltViewModel()
) {
    val currentLocation by viewModel.currentLocation.collectAsStateWithLifecycle()
    val targetLocation by viewModel.targetLocation.collectAsStateWithLifecycle()
    val routePoints by viewModel.routePoints.collectAsStateWithLifecycle()
    val isTracking by viewModel.isTracking.collectAsStateWithLifecycle()
    val distanceToTarget by viewModel.distanceToTarget.collectAsStateWithLifecycle()
    val alarmTriggered by viewModel.alarmTriggered.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState()

    // Initial camera setup
    LaunchedEffect(currentLocation) {
        if (currentLocation != null) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLocation!!, 12f)
        }
    }

    val uiSettings = remember {
        MapUiSettings(
            myLocationButtonEnabled = false,
            zoomControlsEnabled = false
        )
    }

    val properties = remember {
        MapProperties(
            isMyLocationEnabled = true,
            mapStyleOptions = com.google.android.gms.maps.model.MapStyleOptions(
                MapTheme.getDarkMapStyle()
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent) // remove gray background
    ) {
        // 🗺️ MAP in background (fills full screen)
        GoogleMap(
            modifier = Modifier
                .matchParentSize()
                .systemBarsPadding(), // so it goes behind nav bar
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings,
            onMapClick = { latLng ->
                if (!isTracking) viewModel.setTarget(latLng)
            }
        ) {
            currentLocation?.let {
//                Marker(state = MarkerState(position = it), title = "Your Location")
                CurrentLocationMarker(position = it)
            }
            targetLocation?.let {
                PulsatingMarker(position = it)
            }
            if (routePoints.isNotEmpty()) {
                Polyline(
                    points = routePoints,
                    color = Color.Black,
                    width = 12f
                )
            }
        }

        // 🔹 Header on top of the map
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xAA1976D2), Color(0xAA1565C0))
                    )
                )
                .padding(16.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Route Tracker",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    if (!isTracking) {
                        viewModel.drawRoute()
                        currentLocation?.let {
                            coroutineScope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(it, 16f),
                                    1000
                                )
                            }
                        }
                    } else {
                        viewModel.stopTracking()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isTracking) Color.Red else Color.White,
                    contentColor = if (isTracking) Color.White else Color(0xFF1976D2)
                )
            ) {
                Text(if (isTracking) "Stop Tracking" else "Draw Route & Start")
            }
        }


        // 📍 My Location Button (overlay)
        FloatingActionButton(
            onClick = {
                currentLocation?.let {
                    coroutineScope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(it, 16f),
                            1000
                        )
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(40.dp)
            ,

            containerColor = Color.White
        ) {
//            Icon(Icons.Default.LocationOn, "My Location")
            Icon(
                painter = painterResource(id = R.drawable.gps_ic),
                contentDescription = "Icon description",
                modifier = Modifier.size(24.dp)
            )        }

        // 🚨 Alarm overlay
        if (alarmTriggered) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Alarm Triggered!",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
