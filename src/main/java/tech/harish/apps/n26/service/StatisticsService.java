package tech.harish.apps.n26.service;

import tech.harish.apps.n26.dto.Transaction;

public interface StatisticsService {
    Double getSum();

    Double getAverage();

    Double getMaximum();

    Double getMinimum();

    Long getCount();

    void recordTransaction(Transaction transaction);
}
