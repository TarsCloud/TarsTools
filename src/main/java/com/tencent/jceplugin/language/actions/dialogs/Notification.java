package com.tencent.jceplugin.language.actions.dialogs;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;

/**
 * @author kongyuanyuan
 * @since 2021/11/8
 */
public class Notification {
    public static void showTip(String title, String message) {
        NotificationType notificationType = NotificationType.INFORMATION;
        showTip(title, message, notificationType);
    }

    public static void showTip(String title, String message, NotificationType notificationType) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("Tencent Jce Support")
                .createNotification(title, message, notificationType)
                .notify(null);
    }
}
