package com.bank.transfer.dtos;

public class TransferRequest {
    private String loginFrom;

    private String loginTo;

    private double sum;

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

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
