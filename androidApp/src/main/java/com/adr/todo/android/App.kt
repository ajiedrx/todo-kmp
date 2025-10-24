package com.adr.todo.android

import android.app.Application
import com.adr.todo.di.initKoin
import org.koin.android.ext.koin.androidContext

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@App)
        }
    }
}