//package com.example.clockapp.presentation.tracking_screen
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.clockapp.domain.tracking.TrackRepo
//import com.google.android.gms.maps.model.LatLng
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//
//@HiltViewModel
//class TrackViewModel @Inject constructor(
//    private val trackRepo: TrackRepo
//) : ViewModel() {
//    //.stateIn(...) to turn it into a StateFlow
//    // Convert the Flow from repo into a StateFlow so Compose can easily observe it
//    val currentLocation =
//        trackRepo.getCurrentLocationUpdates() //viewModelScope: keeps collecting as long as ViewModel is alive , null the initial value before the first location arrives.
//            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
//
//     val targetLocation = trackRepo.getTargetLocation()
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
//
//    //  List of LatLng points that form the route line on the map ---
//    private val _routePoints = MutableStateFlow<List<LatLng>>(emptyList())
//    val routePoints = _routePoints.asStateFlow()
//
//    // Called when user taps on the map to select a target location ---
//    fun setTarget(latLng: LatLng) {
//        viewModelScope.launch {
//            trackRepo.setTargetLocation(latLng)
//        }
//    }
//
//    // Called when user clicks "Draw Route" to show path on map ---
////    fun drawRoute() {
////        viewModelScope.launch {
////            // Get the latest current and target locations
////            val current = currentLocation.value
////            val target = targetLocation.value
////
////            // If both exist, ask repo for route points between them
////            if (current != null && target != null) {
////                val route = trackRepo.getRouteBetweenLocations(current, target)
////                // Update the state so UI can draw the line
////                _routePoints.value = route
////            }
////        }
////    }
//
//    fun drawRoute() {
//        viewModelScope.launch {
//            val current = currentLocation.value // <-- Put a breakpoint here
//            val target = targetLocation.value   // <-- Put a breakpoint here
//
//            if (current != null && target != null) {
//                val route = trackRepo.getRouteBetweenLocations(current, target)
//                _routePoints.value = route // <-- Put a breakpoint here
//            }
//        }
//    }
//}
package com.example.clockapp.presentation.tracking_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clockapp.domain.tracking.TrackRepo
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@HiltViewModel
class TrackViewModel @Inject constructor(
    private val trackRepo: TrackRepo
) : ViewModel() {
    val currentLocation =
        trackRepo.getCurrentLocationUpdates()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val targetLocation = trackRepo.getTargetLocation()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _routePoints = MutableStateFlow<List<LatLng>>(emptyList())
    val routePoints = _routePoints.asStateFlow()

    // NEW: Track if user is currently tracking to destination
    private val _isTracking = MutableStateFlow(false)
    val isTracking = _isTracking.asStateFlow()

    // NEW: Distance to destination in meters
    private val _distanceToTarget = MutableStateFlow(0f)
    val distanceToTarget = _distanceToTarget.asStateFlow()

    // NEW: Whether the alarm has been triggered
    private val _alarmTriggered = MutableStateFlow(false)
    val alarmTriggered = _alarmTriggered.asStateFlow()

    fun setTarget(latLng: LatLng) {
        viewModelScope.launch {
            trackRepo.setTargetLocation(latLng)
        }
    }

    fun drawRoute() {
        viewModelScope.launch {
            val current = currentLocation.value
            val target = targetLocation.value

            if (current != null && target != null) {
                try {
                    val route = trackRepo.getRouteBetweenLocations(current, target)
                    _routePoints.value = route
                    _isTracking.value = true
                    _alarmTriggered.value = false // Reset alarm
                    startTrackingToDestination()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    // NEW: Continuously monitor distance to target
    private fun startTrackingToDestination() {
        viewModelScope.launch {
            currentLocation.collect { current ->
                if (_isTracking.value && current != null) {
                    val target = targetLocation.value
                    if (target != null) {
                        val distance = calculateDistance(current, target)
                        _distanceToTarget.value = distance

                        // Trigger alarm if within 20 meters (adjust as needed)
                        if (distance < 20f && !_alarmTriggered.value) {
                            _alarmTriggered.value = true
                            triggerAlarm()
                        }
                    }
                }
            }
        }
    }

    // NEW: Calculate distance between two points in meters
    private fun calculateDistance(start: LatLng, end: LatLng): Float {
        val earthRadiusMeters = 6371000f
        val dLat = Math.toRadians(end.latitude - start.latitude)
        val dLng = Math.toRadians(end.longitude - start.longitude)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(start.latitude)) * cos(Math.toRadians(end.latitude)) *
                sin(dLng / 2) * sin(dLng / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadiusMeters * c.toFloat()
    }

    // NEW: Trigger the alarm when destination is reached
    private fun triggerAlarm() {
        // TODO: Call your alarm service here
        // Example: context.startService(Intent(context, AlarmService::class.java))
        // For now, just log it
        println("ALARM! User has reached destination!")
    }

    fun stopTracking() {
        _isTracking.value = false
        _routePoints.value = emptyList()
        _distanceToTarget.value = 0f
    }
}