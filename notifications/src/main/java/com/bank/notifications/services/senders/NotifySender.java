package com.bank.notifications.services.senders;

public interface NotifySender {

    void send(String toUserId, String message);
}
