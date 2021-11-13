package com.tencent.jceplugin.language.actions.dialogs;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;

/**
 * @author kongyuanyuan
 * @since 2021/11/8
 */
public class Notification {
    private static final NotificationGroup GROUP = new NotificationGroup(
            "Tencent Jce Support",
            NotificationDisplayType.BALLOON,
            false,
            "Tencent Jce Support"
    );

    public static void showTip(String title, String message) {
        NotificationType notificationType = NotificationType.INFORMATION;
        showTip(title, message, notificationType);
    }

    public static void showTip(String title, String message, NotificationType notificationType) {
        final com.intellij.notification.Notification notification = GROUP.createNotification(title,
                message,
                notificationType, null);
        notification.notify(null);
    }
}
