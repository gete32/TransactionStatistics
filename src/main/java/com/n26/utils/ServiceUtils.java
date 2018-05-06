package com.n26.utils;

import com.n26.dto.Transaction;

import java.util.Collection;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;

public class ServiceUtils {

    /**
     * Number of processor to leave in reserve
     */
    private static final int PROCESSOR_RESERVE = 1;

    /**
     * Number to multiply processors by for number of threads to keep ready.
     */
    private static final int PROCESSOR_MULTIPLIER = 5;

    private static final double accuracy = 0.000000000001D;

    public static int getCorePoolSize() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        return PROCESSOR_RESERVE >= availableProcessors ?
                PROCESSOR_MULTIPLIER : (availableProcessors - PROCESSOR_RESERVE) * PROCESSOR_MULTIPLIER;
    }


    public static boolean isEquals(double val1, double val2) {
        return Math.abs(val1 - val2) < accuracy;
    }

    public static DoubleSummaryStatistics getSummaryStat(final Collection<Transaction> transactions) {
        return transactions.stream().collect(Collectors.summarizingDouble(Transaction::getAmount));
    }
}