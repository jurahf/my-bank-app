package com.bank.accounts.services;

import com.bank.accounts.dtos.AccountDto;
import com.bank.accounts.dtos.AccountUpdateRequest;
import com.bank.accounts.dtos.CashChangeRequest;
import com.bank.accounts.dtos.ShortAccountDto;
import com.bank.accounts.models.AccountModel;
import com.bank.accounts.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository repository;

    @Mock
    private NotificationRemoteService notificationRemoteService;

    @InjectMocks
    private AccountService service;

    private AccountModel model(String id, String name, double money) {
        AccountModel m = new AccountModel();
        m.setId(id);
        m.setName(name);
        m.setBirthDate(new Date());
        m.setMoney(BigDecimal.valueOf(money));
        return m;
    }

    @Test
    void getAllReturnsShortAccounts() {
        when(repository.findAll()).thenReturn(List.of(model("u1", "User One", 100), model("u2", "User Two", 200)));

        List<ShortAccountDto> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals("u1", result.get(0).getId());
        assertEquals("User Two", result.get(1).getName());
    }

    @Test
    void getByLoginReturnsAccount() {
        when(repository.findById("u1")).thenReturn(Optional.of(model("u1", "User One", 100)));

        AccountDto result = service.getByLogin("u1");

        assertEquals("u1", result.getId());
        assertEquals("User One", result.getName());
        assertEquals(BigDecimal.valueOf(100), result.getMoney());
    }

    @Test
    void getByLoginThrowsNotFound() {
        when(repository.findById("u1")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.getByLogin("u1"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void updateSavesAndNotifies() {
        when(repository.findById("u1")).thenReturn(Optional.of(model("u1", "Old", 100)));

        AccountUpdateRequest req = new AccountUpdateRequest();
        req.setName("New");

        AccountDto result = service.update("u1", req);

        assertEquals("New", result.getName());
        verify(repository).save(any(AccountModel.class));
        verify(notificationRemoteService).accountEdited(any(AccountDto.class));
    }

    @Test
    void cashChangeIncreasesBalance() {
        when(repository.findByIdWithPessimisticLock("u1")).thenReturn(Optional.of(model("u1", "User One", 100)));
        when(repository.findById("u1")).thenReturn(Optional.of(model("u1", "User One", 150)));

        CashChangeRequest req = new CashChangeRequest();
        req.setDelta(BigDecimal.valueOf(50));

        AccountDto result = service.cashChange("u1", req);

        assertEquals(BigDecimal.valueOf(150), result.getMoney());
        verify(notificationRemoteService).cashChanged(any(AccountDto.class), eq(BigDecimal.valueOf(50)));
    }

    @Test
    void cashChangeRejectsNegativeBalance() {
        when(repository.findByIdWithPessimisticLock("u1")).thenReturn(Optional.of(model("u1", "User One", 10)));

        CashChangeRequest req = new CashChangeRequest();
        req.setDelta(BigDecimal.valueOf(-50));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.cashChange("u1", req));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        verify(repository, never()).save(any());
    }
}
