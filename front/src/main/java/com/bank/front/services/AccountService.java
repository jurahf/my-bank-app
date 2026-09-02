package com.bank.front.services;

import com.bank.front.dtos.AccountShortDto;
import com.bank.front.dtos.CashAction;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestClient;

import javax.naming.Name;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {

    @Autowired
    private RestClient restClient;

    private String gatewayBaseUrl = "http://gatewayapi";

    public void fillModel(Model model, String login, @Nullable List<String> errors, @Nullable String info) {
        Map<String, Object> account = restClient.get()
                .uri(gatewayBaseUrl + "/api/account/{login}", login)
                .retrieve()
                .body(Map.class);

        Map<String, String>[] rawAccounts = restClient.get()
                .uri(gatewayBaseUrl + "/api/account")
                .retrieve()
                .body(Map[].class);

        List<AccountShortDto> accounts = Arrays.stream(rawAccounts)
                .filter(a -> !login.equals(a.get("id")))
                .map(a -> new AccountShortDto(a.get("id"), a.get("name")))
                .toList();

        LocalDate birthdate = OffsetDateTime.parse((String)account.get("birthDate"))
            .toLocalDate();

        model.addAttribute("name", account.get("name"));
        model.addAttribute("birthdate", birthdate.format(DateTimeFormatter.ISO_DATE));
        model.addAttribute("sum", account.get("money"));
        model.addAttribute("accounts", accounts);
        model.addAttribute("errors", errors);
        model.addAttribute("info", info);
    }

    public void updateAccount(String login, String name, LocalDate birthDate) {
        restClient.put()
                .uri(gatewayBaseUrl + "/api/account/{login}", login)
                .body(Map.of(
                        "name", name,
                        "birthDate", birthDate.toString()))
                .retrieve();
    }

    public void updateCash(String login, double value, CashAction action) {

        double delta = value;
        if (action == CashAction.GET)
            delta = -delta;

        restClient.post()
                .uri(gatewayBaseUrl + "/api/cash")
                .body(Map.of(
                        "login", login,
                        "delta", Double.toString(delta)))
                .retrieve();
    }

    public void transfer(String loginFrom, String loginTo, double sum) {
        restClient.post()
                .uri(gatewayBaseUrl + "/api/transfer")
                .body(Map.of(
                        "loginFrom", loginFrom,
                        "loginTo", loginTo,
                        "sum", Double.toString(sum)))
                .retrieve();
    }
}
