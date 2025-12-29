package com.example.clockapp.presentation.tracking_screen

object MapTheme {

    fun getDarkMapStyle(): String {
        return """
        [
          {
            "elementType": "geometry",
            "stylers": [
              { "color": "#eaeaea" }  // softer light gray base
            ]
          },
          {
            "elementType": "labels.icon",
            "stylers": [
              { "visibility": "off" }
            ]
          },
          {
            "elementType": "labels.text.fill",
            "stylers": [
              { "color": "#2b2b2b" }  // clean dark gray for labels
            ]
          },
          {
            "elementType": "labels.text.stroke",
            "stylers": [
              { "color": "#ffffff" }  // white stroke for clarity
            ]
          },
          {
            "featureType": "administrative",
            "elementType": "geometry",
            "stylers": [
              { "color": "#bdbdbd" }  // light gray borders
            ]
          },
          {
            "featureType": "poi",
            "stylers": [
              { "visibility": "off" }
            ]
          },
          {
            "featureType": "road",
            "elementType": "geometry",
            "stylers": [
              { "color": "#f5f5f5" }  // white roads
            ]
          },
          {
            "featureType": "road.arterial",
            "elementType": "geometry",
            "stylers": [
              { "color": "#dcdcdc" }  // light gray roads
            ]
          },
          {
            "featureType": "road.highway",
            "elementType": "geometry",
            "stylers": [
              { "color": "#cfcfcf" }  // slightly darker for contrast
            ]
          },
          {
            "featureType": "water",
            "elementType": "geometry",
            "stylers": [
              { "color": "#d9d9d9" }  // soft gray water
            ]
          },
          {
            "featureType": "water",
            "elementType": "labels.text.fill",
            "stylers": [
              { "color": "#8a8a8a" }
            ]
          }
        ]
    """.trimIndent()
    }


    fun getLightMapStyle(): String {
        return """
    [
      {
        "elementType": "geometry",
        "stylers": [
          { "color": "#FFFFFF" }
        ]
      },
      {
        "elementType": "labels.icon",
        "stylers": [
          { "visibility": "off" }
        ]
      },
      {
        "elementType": "labels.text.fill",
        "stylers": [
          { "color": "#555555" }
        ]
      },
      {
        "elementType": "labels.text.stroke",
        "stylers": [
          { "color": "#FFFFFF" }
        ]
      },
      {
        "featureType": "road",
        "elementType": "geometry",
        "stylers": [
          { "color": "#E0E0E0" }
        ]
      },
      {
        "featureType": "road.highway",
        "stylers": [
          { "color": "#CCCCCC" }
        ]
      },
      {
        "featureType": "water",
        "stylers": [
          { "color": "#F2F2F2" }
        ]
      },
      {
        "featureType": "poi",
        "stylers": [
          { "visibility": "off" }
        ]
      }
    ]
    """.trimIndent()
    }
}