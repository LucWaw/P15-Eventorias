package com.openclassrooms.eventorias

import android.app.Application
import com.openclassrooms.eventorias.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class EventoriasApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@EventoriasApp)
            androidLogger()

            modules(appModule)
        }
    }

}