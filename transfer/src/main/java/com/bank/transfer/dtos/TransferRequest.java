package com.bank.transfer.dtos;

import java.math.BigDecimal;

public class TransferRequest {
    private String loginFrom;

    private String loginTo;

    private BigDecimal sum;

    public String getLoginFrom() {
        return loginFrom;
    }

    public void setLoginFrom(String loginFrom) {
        this.loginFrom = loginFrom;
    }

    public String getLoginTo() {
        return loginTo;
    }

    public void setLoginTo(String loginTo) {
        this.loginTo = loginTo;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
