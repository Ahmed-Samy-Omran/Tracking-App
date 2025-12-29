package com.example.clockapp.presentation.components

sealed class Screen(val route: String)  {
    object ClockScreen : Screen("clock_screen")
    object WorldClockScreen : Screen("world_clock_screen")
    object TimerScreen : Screen("timer_screen")
    object TrackingScreen : Screen("tracking_screen")
}