package com.bank.accounts.services;

import com.bank.accounts.dtos.AccountDto;
import com.bank.accounts.dtos.AccountUpdateRequest;
import com.bank.accounts.dtos.CashChangeRequest;
import com.bank.accounts.dtos.ShortAccountDto;
import com.bank.accounts.models.AccountModel;
import com.bank.accounts.repositories.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository repository;
    private final NotificationRemoteService notificationRemoteService;

    public AccountService(AccountRepository repository, NotificationRemoteService notificationRemoteService) {
        this.repository = repository;
        this.notificationRemoteService = notificationRemoteService;
    }

    public List<ShortAccountDto> getAll() {
        List<AccountModel> models = repository.findAll();

        return models.stream()
                .map(x -> new ShortAccountDto(x.getId(), x.getName()))
                .toList();
    }

    public AccountDto getByLogin(String login) {
        Optional<AccountModel> modelOpt = repository.findById(login);

        if (modelOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        AccountModel model = modelOpt.get();

        return new AccountDto(model.getId(), model.getName(), model.getBirthDate(), model.getMoney());
    }

    public AccountDto update(String login, AccountUpdateRequest request) {
        Optional<AccountModel> modelOpt = repository.findById(login);

        if (modelOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        AccountModel model = modelOpt.get();

        model.setName(request.getName());
        model.setBirthDate(request.getBirthDate());

        repository.save(model);

        var result = getByLogin(login);
        notificationRemoteService.accountEdited(result);

        return result;
    }

    public AccountDto cashChange(String login, CashChangeRequest request) {
        Optional<AccountModel> modelOpt = repository.findById(login);

        if (modelOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        AccountModel model = modelOpt.get();

        double summ = model.getMoney() + request.delta;
        if (summ < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        model.setMoney(summ);
        repository.save(model);

        AccountDto result = getByLogin(login);
        notificationRemoteService.cashChanged(result, request.delta);

        return result;
    }
}
