package com.bank.loanorigination.controller;

import static com.bank.loanorigination.Utils.ApiPaths.PING;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping(PING)
    public String ping() {
        return "pong";
    }
}
