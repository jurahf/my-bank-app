package com.bank.accounts.dtos;


import java.math.BigDecimal;
import java.util.Date;

public class AccountDto {

    public AccountDto(String id, String name, Date birthDate, BigDecimal money) {
        this.id = id;
        this.name = name;
        BirthDate = birthDate;
        this.money = money;
    }

    private String id;

    private String name;

    private Date BirthDate;

    private BigDecimal money;

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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
