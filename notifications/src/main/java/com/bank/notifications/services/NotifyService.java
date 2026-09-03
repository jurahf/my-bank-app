package com.bank.notifications.services;

import com.bank.notifications.services.senders.NotifySender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

    public String cashChanged(String userId, String userName, BigDecimal sum) {
       String text = "";
       if (sum.compareTo(BigDecimal.ZERO) < 0)
           text = "Списание на " + sum.toPlainString();
       else
           text = "Пополнение на " + sum.toPlainString();

        sender.send(userId, text);

        return text;
    }
}
