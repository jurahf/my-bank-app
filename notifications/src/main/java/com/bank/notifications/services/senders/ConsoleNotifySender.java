package com.bank.notifications.services.senders;

public class ConsoleNotifySender implements NotifySender {
    @Override
    public void send(String toUserId, String message) {
        System.out.println(toUserId + ": " + message);
    }
}
