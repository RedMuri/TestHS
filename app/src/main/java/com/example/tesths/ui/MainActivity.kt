package com.example.tesths.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tesths.TestHS
import com.example.tesths.ui.theme.TestHSTheme
import com.example.tesths.ui.viewmodel.ViewModelFactory
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    private val component by lazy {
        (application as TestHS).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            TestHSTheme {
                MainScreen()
            }
        }
    }
}
