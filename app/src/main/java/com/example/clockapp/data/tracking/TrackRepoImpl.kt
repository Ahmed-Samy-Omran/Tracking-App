//package com.example.clockapp.data.tracking
//
//import android.annotation.SuppressLint
//import android.util.Log
//import com.example.clockapp.domain.tracking.DirectionsApiService
//import com.example.clockapp.domain.tracking.TrackRepo
//import com.example.clockapp.utils.decodePolyline
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationRequest
//import com.google.android.gms.location.LocationResult
//import com.google.android.gms.location.Priority
//import com.google.android.gms.maps.model.LatLng
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.withContext
//import javax.inject.Inject
//
//const val DIRECTIONS_API_KEY = "AIzaSyC0Shg3OM3Tym_xo2Jm-AgkIur_jNt7iXQ"
//
//
//class TrackRepoImpl @Inject constructor(
//    private val fusedClient: FusedLocationProviderClient,
//    private val directionsApi: DirectionsApiService,
//    private val ioDispatcher: CoroutineDispatcher
//) : TrackRepo {
//
//
//
//    // This will hold the target (user selected) location
//    private val targetLocation = MutableStateFlow<LatLng?>(null)
//
//    //Continuously emits the user's current location.
//    @SuppressLint("MissingPermission")
//    override fun getCurrentLocationUpdates(): Flow<LatLng> = callbackFlow {
//        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000L)
//            .setMinUpdateDistanceMeters(10f) // update only if user moves 10 meters
//            .build()
//
//        val callback = object : LocationCallback() {
//            //This function runs every time Android gives you a new location.
//            override fun onLocationResult(result: LocationResult) {
//                result.lastLocation?.let {
//                    //it gives you a channel, and you can “send” new data through it.
//                    trySend(LatLng(it.latitude, it.longitude))
//                }
//            }
//        }
//        // Start giving me location updates according to the settings in request and send them to my callback
//        fusedClient.requestLocationUpdates(
//            request,
//            callback,
//            null
//        ) // null means use default ThreadLooper
//        awaitClose { fusedClient.removeLocationUpdates(callback) }//When the user leaves the screen or cancels tracking, stop getting location updates
//    }
//
//
//    override suspend fun setTargetLocation(latLng: LatLng) {
//        targetLocation.emit(latLng) // When the user taps o n the map, you call this function and put the chosen location inside that box.
//    }
//
//    override fun getTargetLocation(): Flow<LatLng?> =
//        targetLocation.asStateFlow() //asStateFlow() converts a MutableStateFlow into a read-only StateFlow.
//
//    override suspend fun getRouteBetweenLocations(start: LatLng, end: LatLng): List<LatLng> =
//        withContext(ioDispatcher) {
//            val originStr = "${start.latitude},${start.longitude}"
//            val destinationStr = "${end.latitude},${end.longitude}"
//
////            val apiKey ="AIzaSyC8LzBxJqa3I5NuBnl28GMKfL1SzYlbCWg"
//
//            try {
//                val response = directionsApi.getDirections(originStr, destinationStr, DIRECTIONS_API_KEY)
//                Log.d("Directions", "Response code: ${response.code()}")
//                Log.d("Directions", "Response body: ${response.body()?.routes?.size}")
//
//                if (response.isSuccessful) {
//                    val body = response.body()
//                    val route = body?.routes?.firstOrNull()
//                    if (body != null && route != null) {
//                        Log.d("Directions", "Polyline: ${route.overviewPolyline.points}")
//                        return@withContext decodePolyline(route.overviewPolyline.points)
//                    }
//                } else {
//                    Log.e("Directions", "Error body: ${response.errorBody()?.string()}")
//                }
//                emptyList()
//            } catch (e: Exception) {
//                Log.e("Directions", "Exception: ${e.message}")
//                emptyList()
//            }
//        }
//
////    override suspend fun getRouteBetweenLocations(start: LatLng, end: LatLng): List<LatLng> =
////        withContext(ioDispatcher) {
////            val originStr = "${start.latitude},${start.longitude}"
////            val destinationStr = "${end.latitude},${end.longitude}"
////
////            // IMPORTANT: Do NOT hardcode your API key.
////            // Use BuildConfig or another secure method.
////            val apiKey = "AIzaSyC8LzBxJqa3I5NuBnl28GMKfL1SzYlbCWg"
////
////            try {
////                val response = directionsApi.getDirections(originStr, destinationStr, apiKey)
////                if (response.isSuccessful) {
////                    val body = response.body()
////                    val route = body?.routes?.firstOrNull()
////                    if (body != null && route != null) {
////                        // Decode the polyline and return the list of points
////                        return@withContext decodePolyline(route.overviewPolyline.points)
////                    }
////                }
////                // If response fails or has no routes, return an empty list
////                return@withContext emptyList<LatLng>()
////            } catch (e: Exception) {
////                // Handle exceptions (e.g., no internet)
////                e.printStackTrace()
////                return@withContext emptyList<LatLng>()
////            }
////        }
//}
////        withContext(ioDispatcher) {
////            listOf(start, end) // Placeholder for actual route fetching logic
////        }
//
package com.example.clockapp.data.tracking

import android.annotation.SuppressLint
import android.util.Log
import com.example.clockapp.BuildConfig
import com.example.clockapp.domain.tracking.DirectionsApiService
import com.example.clockapp.domain.tracking.TrackRepo
import com.example.clockapp.utils.decodePolyline
import com.google.android.gms.location.Priority
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class TrackRepoImpl @Inject constructor(
    private val fusedClient: FusedLocationProviderClient,
    private val directionsApi: DirectionsApiService,
    private val ioDispatcher: CoroutineDispatcher
) : TrackRepo {

    private val targetLocation = MutableStateFlow<LatLng?>(null)

    @SuppressLint("MissingPermission")
    override fun getCurrentLocationUpdates(): Flow<LatLng> = callbackFlow {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000L)
            .setMinUpdateDistanceMeters(10f)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let {
                    trySend(LatLng(it.latitude, it.longitude))
                }
            }
        }

        fusedClient.requestLocationUpdates(request, callback, null)
        awaitClose { fusedClient.removeLocationUpdates(callback) }
    }

    override suspend fun setTargetLocation(latLng: LatLng) {
        targetLocation.emit(latLng)
    }

    override fun getTargetLocation(): Flow<LatLng?> =
        targetLocation.asStateFlow()

    override suspend fun getRouteBetweenLocations(start: LatLng, end: LatLng): List<LatLng> =
        withContext(ioDispatcher) {
            val originStr = "${start.latitude},${start.longitude}"
            val destinationStr = "${end.latitude},${end.longitude}"



            try {
                // ✅ Use API key from BuildConfig
                val apiKey = BuildConfig.DIRECTIONS_API_KEY
                Log.d("TrackRepo", "Using API key: ${apiKey.take(20)}...") // Log first 20 chars only (for security)

                Log.d("TrackRepo", "Requesting directions from $originStr to $destinationStr")

                val response = directionsApi.getDirections(originStr, destinationStr, apiKey)


                Log.d("Directions", "Response code: ${response.code()}")
                Log.d("Directions", "Response successful: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("Directions", "Routes found: ${body?.routes?.size ?: 0}")
                    Log.d("Directions", "Full response: ${body.toString()}")

//                    Log.d("Directions", "Origin: $originStr")
//                    Log.w("Directions", "Status: ${body?.status}")


                    val route = body?.routes?.firstOrNull()
                    if (body != null && route != null) {
                        val polylinePoints = route. overviewPolyline.points
                        Log.d("Directions", "Polyline points: $polylinePoints")

                        val decodedPoints = decodePolyline(polylinePoints)
                        Log.d("Directions", "Decoded ${decodedPoints.size} points")

                        return@withContext decodedPoints
                    } else {
                        Log.w("Directions", "No routes found in response - check if both locations are on roads")
                        Log.w("Directions", "Status: ${body?.status}")
                        // Fallback: return a direct line between the two points
//                        return@withContext listOf(start, end)
                        return@withContext emptyList()

                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Directions", "API Error (${response.code()}): $errorBody")

                    // Common error codes
                    when (response.code()) {
                        403 -> Log.e("Directions", "API key is invalid or Directions API is not enabled")
                        401 -> Log.e("Directions", "Unauthorized - check your API key")
                        400 -> Log.e("Directions", "Bad request - check parameters")
                    }
                }

                emptyList()
            } catch (e: Exception) {
                Log.e("Directions", "Exception getting directions: ${e.message}", e)
                e.printStackTrace()
                emptyList()
            }
        }
}