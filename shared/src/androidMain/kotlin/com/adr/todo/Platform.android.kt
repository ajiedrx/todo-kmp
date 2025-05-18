package com.adr.todo

import androidx.activity.ComponentActivity

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual class ContextFactory(private val activity: ComponentActivity) {
    actual fun getContext(): Any = activity.baseContext
    actual fun getApplication(): Any = activity.application
    actual fun getActivity(): Any = activity
}
