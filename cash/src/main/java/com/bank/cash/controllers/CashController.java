package com.bank.cash.controllers;

import com.bank.cash.dtos.CashRequest;
import com.bank.cash.services.CashService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cash")
public class CashController {

    private final CashService cashService;

    public CashController(CashService cashService) {
        this.cashService = cashService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CASH')")
    public boolean getOrPutMoney(@RequestBody CashRequest request) {
        return cashService.getOrPutMoney(request);
    }
}
