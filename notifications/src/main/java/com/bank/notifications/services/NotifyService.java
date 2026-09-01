package com.bank.notifications.services;

import com.bank.notifications.services.senders.NotifySender;
import org.springframework.stereotype.Service;

@Service
public class NotifyService {

    private final NotifySender sender;

    public NotifyService(NotifySender sender) {
        this.sender = sender;
    }

    public String accountEdited(String userId, String userName) {
        String text = "Учетная запись пользователя " + userName + " отредактирована";

        sender.send(userId, text);

        return text;
    }

    public String cashChanged(String userId, String userName, double sum) {
       String text = "";
       if (sum < 0)
           text = "Списание на " + Double.toString(sum);
       else
           text = "Пополнение на " + Double.toString(sum);

        sender.send(userId, text);

        return text;
    }
}
