package com.example.tesths.ui.navigation

import com.example.tesths.R

sealed class NavigationItem (
    val screen: Screen,
    val titleResId: Int,
    val iconResId: Int,
) {

    object Menu : NavigationItem(
        screen = Screen.Menu,
        titleResId = R.string.navigation_item_menu,
        iconResId = R.drawable.ic_bottom_menu,
    )

    object Profile : NavigationItem(
        screen = Screen.Profile,
        titleResId = R.string.navigation_item_profile,
        iconResId = R.drawable.ic_bottom_profile,
    )

    object Cart : NavigationItem(
        screen = Screen.Cart,
        titleResId = R.string.navigation_item_cart,
        iconResId = R.drawable.ic_bottom_cart,
    )
}
