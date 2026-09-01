package com.bank.notifications.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CashNotifyRequest {

    @NotBlank(message="UserId должен быть заполнен")
    private String userId;

    @NotBlank(message="UserName должен быть заполнен")
    private String userName;

    @NotNull
    private double sum;

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
