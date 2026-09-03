package com.bank.front.dtos;

public class AccountShortDto {

    private String login;

    private String name;

    public AccountShortDto() {
    }

    public AccountShortDto(String login, String name) {
        this.login = login;
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
