package com.bank.transfer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class AccountRemoteService {
    private static final String ACCOUNTS_URL = "http://accounts/api/account";

    @Autowired
    private RestClient restClient;

    public boolean cashChange(String login, BigDecimal delta) {
        try {
            Map<String, String> body = Map.of(
                    "delta", delta.toPlainString()
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
