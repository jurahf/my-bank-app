package com.bank.transfer.services;

import com.bank.transfer.dtos.TransferRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TransferService {

    private final AccountRemoteService accountRemoteService;

    public TransferService(AccountRemoteService accountRemoteService) {
        this.accountRemoteService = accountRemoteService;
    }

    public boolean ExecTransfer(TransferRequest request) {
        if (request.getSum() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        // почти outbox :)  не успеваю сделать для этого базу и хранить в ней статусы и еще отдельный процесс запускать
        boolean firstPart = accountRemoteService.cashChange(request.getLoginFrom(), -request.getSum());

        if (firstPart)
        {
            boolean secondPart = accountRemoteService.cashChange(request.getLoginTo(), request.getSum());

            if (!secondPart)
            {
                // откатываем первую часть
                accountRemoteService.cashChange(request.getLoginFrom(), request.getSum());

                return false;
            }
        }

        return firstPart;
    }
}
