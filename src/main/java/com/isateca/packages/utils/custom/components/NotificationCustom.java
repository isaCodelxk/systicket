package com.isateca.packages.utils.custom.components;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class NotificationCustom extends Notification{

    public static Notification notification;

    public static Notification getTopEndNotificationWarning(String text){
        notification = new Notification();
        notification.setText(text);
        notification.setDuration(3000);
        notification.setPosition(Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
        notification.open();

        return notification;
    }
    
}