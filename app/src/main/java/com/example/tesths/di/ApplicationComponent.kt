package com.example.tesths.di

import android.app.Application
import com.example.macttestapp.di.ApplicationScope
import com.example.tesths.ui.MainActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component()
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface ApplicationComponentFactory {

        fun create(
            @BindsInstance application: Application,
        ): ApplicationComponent
    }
}