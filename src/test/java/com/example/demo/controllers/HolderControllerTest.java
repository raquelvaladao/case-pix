package com.example.demo.controllers;

import com.example.demo.core.services.HolderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = HolderControllerTest.class)
@Import(HolderController.class)
class HolderControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private HolderService holderService;

    @Test
    void createHolder() throws Exception {
        var validInput = "{\"accountType\":\"corrente\",\"agencyNumber\":12,\"accountNumber\":123123,\"holderName\":\"Joao\",\"personType\":\"PJ\"}";

        doNothing().when(holderService).createHolder(any());

        mvc.perform(post("/v1/holder")
                        .content(validInput)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));
    }
}