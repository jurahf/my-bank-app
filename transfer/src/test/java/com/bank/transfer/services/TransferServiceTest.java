package com.bank.transfer.services;

import com.bank.transfer.dtos.TransferRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private AccountRemoteService accountRemoteService;

    @InjectMocks
    private TransferService transferService;

    private TransferRequest request(String from, String to, double sum) {
        TransferRequest req = new TransferRequest();
        req.setLoginFrom(from);
        req.setLoginTo(to);
        req.setSum(BigDecimal.valueOf(sum));
        return req;
    }

    @Test
    void execTransferSuccess() {
        when(accountRemoteService.cashChange("user1", BigDecimal.valueOf(-100))).thenReturn(true);
        when(accountRemoteService.cashChange("user2", BigDecimal.valueOf(100))).thenReturn(true);

        assertTrue(transferService.ExecTransfer(request("user1", "user2", 100)));
        verify(accountRemoteService).cashChange("user1", BigDecimal.valueOf(-100));
        verify(accountRemoteService).cashChange("user2", BigDecimal.valueOf(100));
    }

    @Test
    void execTransferRejectsNonPositiveSum() {
        assertThrows(ResponseStatusException.class, () -> transferService.ExecTransfer(request("a", "b", 0)));
        assertThrows(ResponseStatusException.class, () -> transferService.ExecTransfer(request("a", "b", -10)));
    }

    @Test
    void execTransferRollbackOnSecondPartFailure() {
        when(accountRemoteService.cashChange("user1", BigDecimal.valueOf(-100))).thenReturn(true);
        when(accountRemoteService.cashChange("user2", BigDecimal.valueOf(100))).thenReturn(false);

        assertFalse(transferService.ExecTransfer(request("user1", "user2", 100)));

        verify(accountRemoteService).cashChange("user1", BigDecimal.valueOf(100));
    }

    @Test
    void execTransferFailsIfFirstPartFails() {
        when(accountRemoteService.cashChange("user1", BigDecimal.valueOf(-100))).thenReturn(false);

        assertFalse(transferService.ExecTransfer(request("user1", "user2", 100)));

        verify(accountRemoteService, never()).cashChange(eq("user2"), any(BigDecimal.class));
    }
}
