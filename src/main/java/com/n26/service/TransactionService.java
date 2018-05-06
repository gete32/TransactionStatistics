package com.n26.service;

public interface TransactionService<T, S> {

    void addTransaction(T source) ;

    S getStatistic();

}
