package com.example.demo.controllers;

import com.example.demo.core.services.PixKeyService;
import com.example.demo.dtos.PixKeyIdDTO;
import com.example.demo.dtos.PixKeyResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PixControllerTest.class)
@Import(PixController.class)
class PixControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private PixKeyService pixKeyService;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void addPixKey() throws Exception {
        var validInput = "{\"keyValue\":\"43805714084\",\"keyType\":\"cpf\",\"accountType\":\"corrente\",\"agencyNumber\":12,\"accountNumber\":123123,\"holderName\":\"Joao\",\"holderSurname\":\"Costa S Silva\"}";
        var response = "{\"keyId\":\"1234-asdf-sdfsd-adf\"}";

        when(pixKeyService.includeKey(any())).thenReturn(PixKeyIdDTO.builder().keyId("1234-asdf-sdfsd-adf").build());

        mvc.perform(post("/v1/key")
                        .content(validInput)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(res -> {
                    Assertions.assertNotNull(res);
                    Assertions.assertEquals(response, res.getResponse().getContentAsString());
                });
    }

    @Test
    void deactivateKey() throws Exception {
        var validInput = "{\"keyId\":\"1234-asdf-sdfsd-adf\"}";
        var response = "{\"keyId\":\"1234-asdf-sdfsd-adf\"}";

        when(pixKeyService.deactivateKey(any())).thenReturn(
                PixKeyResponseDTO.builder().keyId("1234-asdf-sdfsd-adf").build()
        );

        mvc.perform(patch("/v1/key")
                        .content(validInput)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(res -> {
                    Assertions.assertNotNull(res);
                    Assertions.assertEquals(response, res.getResponse().getContentAsString());
                });

    }

    @Test
    void filterByCriteria() throws Exception {
        var response = "[{\"keyId\":\"123456\"}]";

        when(pixKeyService.filter(any())).thenReturn(List.of(PixKeyResponseDTO.builder().keyId("123456").build()));

        mvc.perform(get("/v1/key?keyId=123456")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(res -> {
                    Assertions.assertNotNull(res);
                    Assertions.assertEquals(response, res.getResponse().getContentAsString());
                });
    }

    @Test
    void editKey() throws Exception {
        var validInput = "{\"keyId\":\"123456\",\"accountType\":\"corrente\",\"agencyNumber\":12,\"accountNumber\":123123,\"holderName\":\"Joao\",\"holderSurname\":\"Costa S Silva\"}";
        var response = "{\"keyId\":\"123456\"}";

        when(pixKeyService.editKey(any())).thenReturn(PixKeyResponseDTO.builder().keyId("123456").build());

        mvc.perform(put("/v1/key")
                        .content(validInput)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(res -> {
                    Assertions.assertNotNull(res);
                    Assertions.assertEquals(response, res.getResponse().getContentAsString());
                });
    }
}