package com.bank.cash.controllers;

import com.bank.cash.dtos.CashRequest;
import com.bank.cash.services.CashService;
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
    public boolean getOrPutMoney(@RequestBody CashRequest request) {
        // TODO: можно только владельцу счета
        return cashService.getOrPutMoney(request);
    }
}
