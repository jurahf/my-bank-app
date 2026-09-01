package com.bank.accounts.dtos;

import java.util.Date;

public class AccountUpdateRequest {
    private String name;

    private Date BirthDate;

    public Date getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(Date birthDate) {
        BirthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
