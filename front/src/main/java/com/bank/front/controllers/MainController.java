package com.bank.front.controllers;

import com.bank.front.dtos.CashAction;
import com.bank.front.services.AccountService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Контроллер main.html.
 *
 * Используемая модель для main.html:
 *      model.addAttribute("name", name);
 *      model.addAttribute("birthdate", birthdate.format(DateTimeFormatter.ISO_DATE));
 *      model.addAttribute("sum", sum);
 *      model.addAttribute("accounts", accounts);
 *      model.addAttribute("errors", errors);
 *      model.addAttribute("info", info);
 *
 * Поля модели:
 *      name - Фамилия Имя текущего пользователя, String (обязательное)
 *      birthdate - дата рождения текущего пользователя, String в формате 'YYYY-MM-DD' (обязательное)
 *      sum - сумма на счету текущего пользователя, Integer (обязательное)
 *      accounts - список аккаунтов, которым можно перевести деньги, List<AccountDto> (обязательное)
 *      errors - список ошибок после выполнения действий, List<String> (не обязательное)
 *      info - строка успешности после выполнения действия, String (не обязательное)
 *
 * С примерами использования можно ознакомиться в тестовом классе заглушке AccountStub
 */
@Controller
public class MainController {

    private final AccountService accountService;

    public MainController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * GET /.
     * Редирект на GET /account
     */
    @GetMapping
    public String index() {
        return "redirect:/account";
    }

    /**
     * GET /account.
     */
    @GetMapping("/account")
    public String getAccount(Model model) {
        accountService.fillModel(model, getLogin(), null, null);
        return "main";
    }


    /**
     * POST /account.
     */
    @PostMapping("/account")
    public String editAccount(
            Model model,
            @RequestParam("name") String name,
            @RequestParam("birthdate") LocalDate birthdate
    ) {
        String login = getLogin();

        accountService.updateAccount(login, name, birthdate);
        accountService.fillModel(model, login, null, null);

        return "main";
    }

    /**
     * POST /cash.
     * Параметры:
     * 1. value - сумма списания
     * 2. action - GET (снять), PUT (пополнить)
     */
    @PostMapping("/cash")
    public String editCash(
            Model model,
            @RequestParam("value") int value,
            @RequestParam("action") CashAction action
    ) {
        String login = getLogin();

        accountService.updateCash(login, BigDecimal.valueOf(value), action);
        accountService.fillModel(model, login, null, null);

        return "main";
    }

    /**
     * POST /transfer.
     * Параметры:
     * 1. value - сумма списания
     * 2. login - логин пользователя получателя
     */
    @PostMapping("/transfer")
    public String transfer(
            Model model,
            @RequestParam("value") int value,
            @RequestParam("login") String loginTo
    ) {
        String loginFrom = getLogin();

        accountService.transfer(loginFrom, loginTo, BigDecimal.valueOf(value));
        accountService.fillModel(model, loginFrom, null, null);

        return "main";
    }

    private String getLogin() {
        String login = "";

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof OAuth2AuthenticationToken) {
            login = (String) ((OAuth2AuthenticationToken) auth)
                    .getPrincipal()
                    .getAttributes()
                    .get("preferred_username");
        }

        return login;
    }
}
