package com.adr.todo.app

import com.adr.todo.di.diModules
import org.koin.core.context.startKoin
import platform.Foundation.NSNumber
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationDelegateProtocol
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptionSound
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNNotificationResponse
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject

class AppDelegate : NSObject(), UIApplicationDelegateProtocol,
    UNUserNotificationCenterDelegateProtocol {
    private var initialTodoId: Long? = null

    override fun application(
        application: UIApplication,
        didFinishLaunchingWithOptions: Map<Any?, *>?,
    ): Boolean {
        UNUserNotificationCenter.currentNotificationCenter().delegate = this
        initKoin()

        return true
    }

    override fun userNotificationCenter(
        center: UNUserNotificationCenter,
        willPresentNotification: UNNotification,
        withCompletionHandler: (UNNotificationPresentationOptions) -> Unit,
    ) {
        withCompletionHandler(UNNotificationPresentationOptionAlert or UNNotificationPresentationOptionSound)
    }

    override fun userNotificationCenter(
        center: UNUserNotificationCenter,
        didReceiveNotificationResponse: UNNotificationResponse,
        withCompletionHandler: () -> Unit,
    ) {
        val userInfo = didReceiveNotificationResponse.notification.request.content.userInfo

        val todoId = (userInfo["todoId"] as? NSNumber)?.longValue()

        if (todoId != null) {
            initialTodoId = todoId
        }

        withCompletionHandler()
    }

    private fun initKoin() {
        startKoin {
            modules(diModules)
        }
    }

    fun getInitialTodoId(): Long? {
        val id = initialTodoId
        initialTodoId = null
        return id
    }
}