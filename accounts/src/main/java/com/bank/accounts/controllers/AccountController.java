package com.bank.accounts.controllers;

import com.bank.accounts.dtos.AccountDto;
import com.bank.accounts.dtos.AccountUpdateRequest;
import com.bank.accounts.dtos.CashChangeRequest;
import com.bank.accounts.dtos.ShortAccountDto;
import com.bank.accounts.services.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping
    public List<ShortAccountDto> get() {
        return service.getAll();
    }

    @GetMapping("/{login}")
    public AccountDto get(@PathVariable String login) {
        return service.getByLogin(login);
        // TODO: добавить проверку - читать можно только свои данные
    }

    @PutMapping("/{login}")
    public AccountDto update(@PathVariable String login, @RequestBody AccountUpdateRequest request) {
        // TODO: добавить проверку - редактировать можно только свои данные
        return service.update(login, request);
    }

    @PutMapping("/{login}/cashChange")
    public AccountDto cashChange(@PathVariable String login, @RequestBody CashChangeRequest request) {
        // TODO: могут только сервисы с особыми правами
        return service.cashChange(login, request);
    }
}
