package com.bank.notifications.controllers;

import com.bank.notifications.dtos.CashNotifyRequest;
import com.bank.notifications.dtos.NotifyRequest;
import com.bank.notifications.services.NotifyService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notify")
public class NotifyController {

    private final NotifyService service;

    public NotifyController(NotifyService service) {
        this.service = service;
    }

    @PostMapping("/accountEdited")
    public String accountEdited(@RequestBody @Valid NotifyRequest request) {
        return service.accountEdited(request.getUserId(), request.getUserName());
    }

    @PostMapping("/cashChanged")
    public String cashChanged(@RequestBody @Valid CashNotifyRequest request) {
        return service.cashChanged(request.getUserId(), request.getUserName(), request.getSum());
    }
}
