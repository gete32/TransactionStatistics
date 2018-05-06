package com.n26.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.n26.dto.Statistic;
import com.n26.dto.Transaction;
import com.n26.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.DoubleSummaryStatistics;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionServiceImpl implements TransactionService<Transaction, Statistic> {

    @Value("${default.transaction.expirationTime}")
    private static Long expirationTime;

    private static Cache<String, Transaction> transactions;
    private static Stat statistic;

    private enum Operation {ADD, DEDUCT}

    private static class Stat {
        double sum, max, min;
        long count;

        void change(double value, Operation operation) {
            if (Operation.ADD.equals(operation)) {
                max = count == 0 ? value : Math.max(max, value);
                min = count == 0 ? value : Math.min(min, value);
                sum += value;
                count++;
            } else {
                count--;
                sum -= value;
                if (count > 0 && (ServiceUtils.isEquals(max, value) || ServiceUtils.isEquals(min, value))) {
                    DoubleSummaryStatistics doubleSummaryStatistics = ServiceUtils.getSummaryStat(transactions.asMap().values());
                    max = doubleSummaryStatistics.getMax();
                    min = doubleSummaryStatistics.getMin();
                }
            }
        }
    }

    private synchronized void addTransactionStat(Transaction transaction, Operation operation) {
        if (transaction == null) return;
        statistic.change(transaction.getAmount(), operation);
    }

    private void evictExpired(){
        transactions.asMap().forEach((key, value) -> {
            if (value.isExpired()) transactions.invalidate(key);
        });
    }

    @Override
    public void addTransaction(Transaction source) {
        addTransactionStat(source, Operation.ADD);
        transactions.put(source.getUniqueId(), source);
    }

    @Override
    public Statistic getStatistic() {
        evictExpired();
        return statistic.count == 0 ? new Statistic() :
                new Statistic(statistic.sum, statistic.sum / statistic.count, statistic.max, statistic.min, statistic.count);
    }

    @PostConstruct
    public void init() {
        Transaction.setBestBefore(expirationTime);
        statistic = new Stat();
        transactions = CacheBuilder.newBuilder()
                .concurrencyLevel(ServiceUtils.getCorePoolSize())
                .expireAfterWrite(expirationTime, TimeUnit.MILLISECONDS)
                .removalListener(e -> {
                    Transaction transaction = (Transaction) e.getValue();
                    addTransactionStat(transaction, Operation.DEDUCT);
                })
                .build();
    }
}
