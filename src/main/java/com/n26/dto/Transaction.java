package com.n26.dto;

import com.n26.annotations.TimeStampTransaction;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Data
@EqualsAndHashCode
@TimeStampTransaction
public class Transaction {

    private static long BEST_BEFORE = 60000;

    private transient String uniqueId;

    @NotNull
    private Double amount;

    @NotNull
    private Long timeStamp;

    public Transaction() {
        this.uniqueId = UUID.randomUUID().toString();
    }

    public Transaction(@NotNull Double amount, @NotNull Long timeStamp) {
        this();
        this.amount = amount;
        this.timeStamp = timeStamp;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public static void setBestBefore(long bestBefore) {
        BEST_BEFORE = bestBefore;
    }

    public boolean isExpired() {
        long diff = Instant.now().toEpochMilli() - timeStamp;
        return diff < 0 || diff > BEST_BEFORE;
    }

    public Double getAmount() {
        return amount;
    }

    public String getUniqueId() {
        return uniqueId;
    }
}
