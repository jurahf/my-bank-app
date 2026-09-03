package com.bank.transfer.services;

import com.bank.transfer.dtos.TransferRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final AccountRemoteService accountRemoteService;

    public TransferService(AccountRemoteService accountRemoteService) {
        this.accountRemoteService = accountRemoteService;
    }

    public boolean execTransfer(TransferRequest request) {
        if (request.getSum().compareTo(BigDecimal.ZERO) <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        boolean firstPart = accountRemoteService.cashChange(request.getLoginFrom(), request.getSum().negate());

        if (firstPart)
        {
            boolean secondPart = accountRemoteService.cashChange(request.getLoginTo(), request.getSum());

            if (!secondPart)
            {
                accountRemoteService.cashChange(request.getLoginFrom(), request.getSum());

                return false;
            }
        }

        return firstPart;
    }
}
