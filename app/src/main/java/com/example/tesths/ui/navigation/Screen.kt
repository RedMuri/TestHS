package com.example.tesths.ui.navigation

import javax.inject.Inject

sealed class Screen @Inject constructor(val route: String) {
    object Menu: Screen(ROUTE_MENU)
    object Profile : Screen(ROUTE_PROFILE)
    object Cart : Screen(ROUTE_CART)

    companion object {
        private const val ROUTE_MENU = "menu_screen"
        private const val ROUTE_PROFILE = "profile_screen"
        private const val ROUTE_CART = "cart_screen"
    }
}