package com.bank.accounts.services;

import com.bank.accounts.dtos.AccountDto;
import com.bank.accounts.models.AccountModel;
import com.bank.accounts.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public List<AccountDto> GetAll() {
        List<AccountModel> models = repository.findAll();

        return models.stream()
                .map(x -> new AccountDto(x.getId(), x.getName(), x.getBirthDate(), x.getMoney()))
                .toList();
    }
}
