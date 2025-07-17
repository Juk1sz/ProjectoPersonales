package com.bank.loanorigination.controller;

import static com.bank.loanorigination.Utils.ApiPaths.PING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class HealthControllerTest {

    @Test
    void pingReturnsPong() throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new HealthController())
                .build();

        mvc.perform(get(PING))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }
}
