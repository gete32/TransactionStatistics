package com.n26.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.Application;
import com.n26.dto.Statistic;
import com.n26.dto.Transaction;
import com.n26.service.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
@ContextConfiguration(classes = {Application.class})
public class TransactionControllerTest {

    private static final String TRANSACTIONS_PATH = "/transactions";
    private static final String STATISTICS_PATH = "/statistics";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TransactionService<Transaction, Statistic> service;

    @Test
    public void getCreatedStatus() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post(TRANSACTIONS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Transaction(123D, Instant.now().toEpochMilli()))))
                .andExpect(status().isCreated());
    }

    @Test
    public void getNoContentStatus() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post(TRANSACTIONS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getStatisticStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(STATISTICS_PATH))
                .andExpect(status().isOk());
    }

}
