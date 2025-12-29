    package com.example.clockapp.domain.tracking

    import com.google.android.gms.maps.model.LatLng
    import kotlinx.coroutines.flow.Flow

    interface TrackRepo {

        // continuously emit user location updates
        fun  getCurrentLocationUpdates():Flow<LatLng>

        // set target location selected by user
        suspend fun setTargetLocation(latLng: LatLng)

        // get target location
        fun getTargetLocation():Flow<LatLng?>

        // get route between two locations
        suspend fun getRouteBetweenLocations(start:LatLng, end:LatLng):List<LatLng>
    }