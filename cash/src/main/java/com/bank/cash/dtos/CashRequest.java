package com.bank.cash.dtos;

import java.math.BigDecimal;

public class CashRequest {
    private String login;

    private BigDecimal delta;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public BigDecimal getDelta() {
        return delta;
    }

    public void setDelta(BigDecimal delta) {
        this.delta = delta;
    }
}
