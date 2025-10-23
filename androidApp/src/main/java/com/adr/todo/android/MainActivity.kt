package com.adr.todo.android

import android.app.AlarmManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.net.toUri
import com.adr.todo.ContextFactory
import com.adr.todo.app.App
import com.adr.todo.util.Constants

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent().also { intent ->
                    intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    intent.data = "package:$packageName".toUri()
                    startActivity(intent)
                }
            }
        }

        val initialTodoId = intent?.extras?.getLong(Constants.EXTRA_TODO_ID)

        setContent {
            App(ContextFactory(this@MainActivity), initialTodoId)
        }
    }
}