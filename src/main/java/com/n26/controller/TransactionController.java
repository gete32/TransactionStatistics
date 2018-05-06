package com.n26.controller;

import com.n26.dto.Statistic;
import com.n26.dto.Transaction;
import com.n26.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class TransactionController {

    private static HttpStatus DEFAULT_EXCEPTION_HTTP_STATUS = HttpStatus.NO_CONTENT;

    @Autowired
    private TransactionService<Transaction, Statistic> transactionService;

    @PostMapping(value = "/transactions")
    public ResponseEntity postTransaction(@RequestBody @Valid Transaction transaction, Errors errors) {
        if (errors.hasErrors()) return new ResponseEntity(DEFAULT_EXCEPTION_HTTP_STATUS);

        transactionService.addTransaction(transaction);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(value = "/statistics")
    public Statistic getStatistic(){
        return transactionService.getStatistic();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handle(){
        return new ResponseEntity(DEFAULT_EXCEPTION_HTTP_STATUS);
    }

}
