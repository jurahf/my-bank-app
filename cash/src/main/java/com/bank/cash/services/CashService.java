package com.bank.cash.services;

import com.bank.cash.dtos.CashRequest;
import org.springframework.stereotype.Service;

@Service
public class CashService {

    private final AccountRemoteService accountRemoteService;

    public CashService(AccountRemoteService accountRemoteService) {
        this.accountRemoteService = accountRemoteService;
    }

    public boolean getOrPutMoney(CashRequest request) {
        return accountRemoteService.cashChange(request.getLogin(), request.getDelta());
    }
}
