package com.bank.notifications.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NotifyRequest {

    @NotBlank(message="UserId должен быть заполнен")
    private String UserId;

    @NotBlank(message="UserName должен быть заполнен")
    private String UserName;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
