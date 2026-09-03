package com.bank.notifications.configs;

import com.bank.notifications.services.senders.ConsoleNotifySender;
import com.bank.notifications.services.senders.NotifySender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SenderConfig {

    @Bean
    public NotifySender notifySender() {
        return new ConsoleNotifySender();
    }
}
