package com.bank.cash.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AccountRemoteService {
    private static final String ACCOUNTS_URL = "http://accounts/api/account";

    @Autowired
    private RestClient restClient;

    public boolean cashChange(String login, double delta) {
        try {
            Map<String, String> body = Map.of(
                    "delta", Double.toString(delta)
            );
            send("/" + login + "/cashChange", body);
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private void send(String path, Object body) {
        restClient.put()
            .uri(ACCOUNTS_URL + path)
            .body(body)
            .headers(h -> h.setContentType(MediaType.APPLICATION_JSON))
            .retrieve();
    }
}
