package com.bank.transfer.controllers;

import com.bank.transfer.dtos.TransferRequest;
import com.bank.transfer.services.TransferService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('TRANSFER')")
    public boolean Transfer(@RequestBody TransferRequest request) {
        return service.ExecTransfer(request);
    }
}
