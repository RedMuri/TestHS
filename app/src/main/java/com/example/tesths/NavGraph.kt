package com.example.tesths

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetupRootNavGraph(
    navController: NavHostController,
    menuScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit,
    cartScreenContent: @Composable () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Menu.route
    ) {
        composable(route = Screen.Menu.route) {
            menuScreenContent()
        }
        composable(route = Screen.Profile.route) {
            profileScreenContent()
        }
        composable(route = Screen.Cart.route) {
            cartScreenContent()
        }
    }
}