package com.bank.notifications.services;

import com.bank.notifications.services.senders.NotifySender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotifyServiceTest {

    @Mock
    private NotifySender sender;

    @InjectMocks
    private NotifyService notifyService;

    @Test
    void accountEditedSendsCorrectMessage() {
        String text = notifyService.accountEdited("u1", "User One");

        assertTrue(text.contains("User One"));
        assertTrue(text.contains("отредактирована"));
        verify(sender).send("u1", text);
    }

    @Test
    void cashChangedNegativeReturnsDebitText() {
        String text = notifyService.cashChanged("u1", "User One", -100);

        assertTrue(text.contains("Списание"));
        assertTrue(text.contains("-100.0"));
        verify(sender).send("u1", text);
    }

    @Test
    void cashChangedPositiveReturnsCreditText() {
        String text = notifyService.cashChanged("u1", "User One", 200);

        assertTrue(text.contains("Пополнение"));
        assertTrue(text.contains("200.0"));
        verify(sender).send("u1", text);
    }
}