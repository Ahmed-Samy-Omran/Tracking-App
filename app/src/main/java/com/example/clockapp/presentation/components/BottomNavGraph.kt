package com.example.clockapp.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.clockapp.presentation.clock.ClockScreen
import com.example.clockapp.presentation.tracking_screen.TrackingScreen
import com.example.clockapp.presentation.timer_screen.TimerScreen
import com.example.clockapp.presentation.worldclock.WorldClockScreen

//@Composable
//fun BottomNavGraph(navController: NavHostController,
//                   modifier: Modifier = Modifier
//                   ){
//    NavHost(navController = navController, startDestination = Screen.ClockScreen.route) {
//        composable(Screen.TrackingScreen.route) { TrackingScreen(navController) }
//        composable(Screen.TimerScreen.route) { TimerScreen(navController) }
//        composable(Screen.WorldClockScreen.route) { WorldClockScreen(navController) }
//        composable(Screen.ClockScreen.route) { ClockScreen(navController) }
//
//    }
//}
//

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp) // Add this parameter
) {
    NavHost(navController = navController, startDestination = Screen.ClockScreen.route) {
        composable(Screen.TrackingScreen.route) {
            // TrackingScreen gets NO padding - full screen under bottom nav
            TrackingScreen(navController)
        }
        composable(Screen.TimerScreen.route) {
            // Other screens get padding so they don't overlap with bottom nav
            Box(modifier = Modifier.padding(innerPadding)) {
                TimerScreen(navController)
            }
        }
//        composable(Screen.WorldClockScreen.route) {
//            Box(modifier = Modifier.padding(innerPadding)) {
//                WorldClockScreen(navController)
//            }
//        }
        composable(Screen.ClockScreen.route) {
            Box(modifier = Modifier.padding(innerPadding)) {
                ClockScreen(navController)
            }
        }
    }
}