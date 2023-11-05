package com.example.tesths

import android.app.Application
import com.example.macttestapp.di.DaggerApplicationComponent

class TestHS : Application() {

    val component by lazy {
        DaggerApplicationComponent
            .factory()
            .create(this)
    }
}