package com.bank.accounts.dtos;


import java.util.Date;

public class AccountDto {

    public AccountDto(String id, String name, Date birthDate, Double money) {
        this.id = id;
        this.name = name;
        BirthDate = birthDate;
        this.money = money;
    }

    private String id;

    private String name;

    private Date BirthDate;

    private Double money;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(Date birthDate) {
        BirthDate = birthDate;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
