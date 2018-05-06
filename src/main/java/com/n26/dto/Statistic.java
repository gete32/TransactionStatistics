package com.n26.dto;

import lombok.*;

@Data
@EqualsAndHashCode
public class Statistic {

    private Double sum, avg, max, min;
    private Long count;

    public Statistic() {
    }

    public Statistic(Double sum, Double avg, Double max, Double min, Long count) {
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    public Double getSum() {
        return sum;
    }

    public Double getAvg() {
        return avg;
    }

    public Double getMax() {
        return max;
    }

    public Double getMin() {
        return min;
    }

    public Long getCount() {
        return count;
    }
}
