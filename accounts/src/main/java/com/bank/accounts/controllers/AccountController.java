package com.bank.accounts.controllers;

import com.bank.accounts.dtos.AccountDto;
import com.bank.accounts.dtos.AccountUpdateRequest;
import com.bank.accounts.dtos.CashChangeRequest;
import com.bank.accounts.dtos.ShortAccountDto;
import com.bank.accounts.services.AccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PreAuthorize("hasRole('ACCOUNTS')")
    public List<ShortAccountDto> get() {
        return service.getAll();
    }

    @GetMapping("/{login}")
    @PreAuthorize("hasRole('ACCOUNTS')")
    public AccountDto get(@PathVariable String login) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return service.getByLogin(login);
    }

    @PutMapping("/{login}")
    @PreAuthorize("hasRole('ACCOUNTS')")
    public AccountDto update(@PathVariable String login, @RequestBody AccountUpdateRequest request) {
        return service.update(login, request);
    }

    @PutMapping("/{login}/cashChange")
    @PreAuthorize("hasRole('ACCOUNTS')")
    public AccountDto cashChange(@PathVariable String login, @RequestBody CashChangeRequest request) {
        return service.cashChange(login, request);
    }
}
