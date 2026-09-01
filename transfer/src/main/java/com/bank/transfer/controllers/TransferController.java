package com.bank.transfer.controllers;

import com.bank.transfer.dtos.TransferRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    @PostMapping
    public String Transfer(@RequestBody TransferRequest request){
        // TODO: проверить права, может выполнить только loginFrom

        return "not implemented";
    }
}
