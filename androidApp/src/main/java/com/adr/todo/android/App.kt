package com.adr.todo.android

import android.app.Application
import com.adr.todo.di.diModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(diModules)
        }
    }
}