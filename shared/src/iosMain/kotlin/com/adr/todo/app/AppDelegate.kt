package com.adr.todo.app

import com.adr.todo.navigation.NavigationManager
import platform.Foundation.NSNumber
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationDelegateProtocol
import platform.UIKit.UIApplicationState
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
            if (UIApplication.sharedApplication.applicationState == UIApplicationState.UIApplicationStateActive) {
                NavigationManager.instance.navigateTo(
                    NavigationManager.Destination.TodoDetail(todoId)
                )
            } else {
                initialTodoId = todoId
            }
        }

        withCompletionHandler()
    }

    fun getInitialTodoId(): Long? {
        val id = initialTodoId
        initialTodoId = null
        return id
    }
}