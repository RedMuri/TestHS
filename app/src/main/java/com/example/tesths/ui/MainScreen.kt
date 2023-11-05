package com.example.tesths.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tesths.ui.navigation.NavigationItem
import com.example.tesths.ui.navigation.SetupRootNavGraph
import com.example.tesths.ui.screens.MenuScreen
import com.example.tesths.ui.theme.CustomDarkGrey
import com.example.tesths.ui.theme.CustomDarkWhite
import com.example.tesths.ui.theme.CustomRed
import com.example.tesths.ui.theme.Inter

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    Scaffold(bottomBar = {
        BottomBar(
            navController = navController, bottomBarState = bottomBarState
        )
    }) { paddingValues ->
        SetupRootNavGraph(navController = navController, menuScreenContent = {
            MenuScreen(paddingValues)
        }, profileScreenContent = {
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize(), contentAlignment = Alignment.Center){
                Text(text = "Profile")
            }
        }, cartScreenContent = {
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize(), contentAlignment = Alignment.Center){
                Text(text = "Cart")
            }
        })
    }
}

@Composable
private fun BottomBar(navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    AnimatedVisibility(visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            NavigationBar(
                containerColor = CustomDarkWhite,
                modifier = Modifier
                    .shadow(elevation = 8.dp)
                    .height(74.dp)
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                val items = listOf(
                    NavigationItem.Menu, NavigationItem.Profile, NavigationItem.Cart
                )
                items.forEach { item ->

                    val selected = navBackStackEntry?.destination?.hierarchy?.any {
                        it.route == item.screen.route
                    } ?: false

                    NavigationBarItem(selected = selected, onClick = {
                        if (!selected) {
                            val route = item.screen.route
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }, icon = {
                        Icon(
                            painter = painterResource(
                                id = item.iconResId
                            ), contentDescription = null
                        )
                    }, label = {
                        Text(
                            text = stringResource(item.titleResId),
                            fontFamily = Inter,
                            color = if (selected) CustomRed else CustomDarkGrey,
                            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                        )
                    }, colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CustomRed,
                        unselectedIconColor = CustomDarkGrey,
                        indicatorColor = CustomDarkWhite
                    )
                    )
                }
            }
        })
}


