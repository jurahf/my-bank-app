package com.bank.cash.services;

import com.bank.cash.dtos.CashRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CashServiceTest {

    @Mock
    private AccountRemoteService accountRemoteService;

    @InjectMocks
    private CashService cashService;

    @Test
    void getOrPutMoneyReturnsTrueOnSuccess() {
        CashRequest req = new CashRequest();
        req.setLogin("user1");
        req.setDelta(100);

        when(accountRemoteService.cashChange("user1", 100)).thenReturn(true);

        assertTrue(cashService.getOrPutMoney(req));
        verify(accountRemoteService).cashChange("user1", 100);
    }

    @Test
    void getOrPutMoneyReturnsFalseOnFailure() {
        CashRequest req = new CashRequest();
        req.setLogin("user1");
        req.setDelta(-50);

        when(accountRemoteService.cashChange("user1", -50)).thenReturn(false);

        assertFalse(cashService.getOrPutMoney(req));
    }
}