package com.bank.accounts.services;

import com.bank.accounts.dtos.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class NotificationRemoteService {

    private static final String NOTIFY_URL = "http://notifications/api/notify";

    @Autowired
    private RestClient restClient;

    public void accountEdited(AccountDto account) {
        try {
            Map<String, String> body = Map.of(
                    "userId", account.getId(),
                    "userName", account.getName()
            );

            send("/accountEdited", body);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void cashChanged(AccountDto account, double delta) {
        try {

            Map<String, Object> body = Map.of(
                    "userId", account.getId(),
                    "userName", account.getName(),
                    "sum", delta
            );

            send("/cashChanged", body);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void send(String path, Object body) {
        restClient.post()
                .uri(NOTIFY_URL + path)
                .body(body)
                .headers(h -> h.setContentType(MediaType.APPLICATION_JSON))
                .retrieve();
    }
}
