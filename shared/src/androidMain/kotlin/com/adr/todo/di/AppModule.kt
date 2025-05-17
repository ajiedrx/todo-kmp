package com.adr.todo.di

import androidx.room.RoomDatabase
import com.adr.todo.data.db.AppDatabase
import com.adr.todo.getDatabaseBuilder
import com.adr.todo.service.NotificationService
import com.adr.todo.util.DateTimeWrapper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun getPlatformSpecificModule() = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        getDatabaseBuilder(get())
    }
    single { NotificationService(androidContext()) }
    factory { DateTimeWrapper.createPlatformInstance() }
}
