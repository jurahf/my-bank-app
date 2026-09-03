package com.bank.accounts.dtos;

import java.math.BigDecimal;

public class CashChangeRequest {
    private BigDecimal delta;

    public BigDecimal getDelta() {
        return delta;
    }

    public void setDelta(BigDecimal delta) {
        this.delta = delta;
    }
}
