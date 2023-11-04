package com.example.tesths

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tesths.ui.theme.CustomRed
import com.example.tesths.ui.theme.Inter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    Scaffold(bottomBar = {
        BottomBar(
            navController = navController, bottomBarState = bottomBarState
        )
    }) {
        SetupRootNavGraph(navController = navController,
            menuScreenContent = {
                MenuScreenRoute()
            },
            profileScreenContent = {
                Text(text = "Profile")
            },
            cartScreenContent = {
                Text(text = "Cart")
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
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .shadow(elevation = 8.dp)
                    .height(75.dp)
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
                            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                        )
                    }, colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CustomRed,
                        unselectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.background
                    )

                    )
                }
            }
        })
}


