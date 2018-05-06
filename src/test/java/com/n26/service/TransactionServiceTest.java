package com.n26.service;

import com.n26.dto.Statistic;
import com.n26.dto.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.n26.utils.ServiceUtils.isEquals;
import static com.n26.utils.ServiceUtils.getSummaryStat;

public class TransactionServiceTest {

    @InjectMocks
    private TransactionServiceImpl service;

    private static final long expirationTime = 6000;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(TransactionServiceImpl.class, "expirationTime", expirationTime);
        this.service.init();
    }

    private Transaction createTransaction(long add){
        return new Transaction(new Random().nextDouble(), Instant.now().toEpochMilli() + add);
    }

    @Test
    public void addTransactionTest(){
        Transaction transaction = createTransaction(expirationTime + 100);
        service.addTransaction(transaction);
        Statistic serviceStat = service.getStatistic();
        Assert.assertNull(serviceStat.getMax());
        Assert.assertNull(serviceStat.getAvg());
        Assert.assertNull(serviceStat.getMin());
        Assert.assertNull(serviceStat.getCount());
    }

    @Test
    public void getStatisticOneTransactionTest(){
        Transaction transaction = createTransaction(0);
        service.addTransaction(transaction);
        Statistic statistic = service.getStatistic();
        Double amount = transaction.getAmount();
        Assert.assertEquals(statistic.getMax(), amount);
        Assert.assertEquals(statistic.getAvg(), amount);
        Assert.assertEquals(statistic.getCount(), new Long(1));
        Assert.assertEquals(statistic.getMin(), amount);
    }

    @Test
    public void getStatisticMultiTest(){
        List<Transaction> transactions = IntStream.range(0, 100).mapToObj(e -> createTransaction(0)).collect(Collectors.toList());
        DoubleSummaryStatistics transactionStat = getSummaryStat(transactions);
        transactions.forEach(e -> service.addTransaction(e));
        Statistic serviceStat = service.getStatistic();
        Assert.assertTrue(isEquals(serviceStat.getSum(), transactionStat.getSum()));
        Assert.assertTrue(isEquals(serviceStat.getMax(), transactionStat.getMax()));
        Assert.assertTrue(isEquals(serviceStat.getAvg(), transactionStat.getAverage()));
        Assert.assertTrue(isEquals(serviceStat.getMin(), transactionStat.getMin()));
        Assert.assertTrue(serviceStat.getCount() == transactionStat.getCount());
    }
}
