package com.fiap.tech.challenge.domain.exceptions;

import com.fiap.tech.challenge.domain.validation.handler.Notification;

public class NotificationException extends DomainException {

    public NotificationException(final String message, final Notification notification) {
        super(message, notification.getErrors());
    }
}
