package tech.harish.apps.n26.service.impl;

import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.harish.apps.n26.dto.Transaction;
import tech.harish.apps.n26.service.StatisticsService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InMemoryStorageStatisticsService implements StatisticsService {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryStorageStatisticsService.class);

    private final List<Transaction> timeSeries = Collections.synchronizedList(new ArrayList<>());

    @Value("${app.series.duration:60}")
    private Long timeSpan;

    private void updateMetrics(){
        logger.debug("Updating metrics");
        Long updatingAt = System.currentTimeMillis();
        synchronized (timeSeries) {
            timeSeries.removeIf(e -> updatingAt - e.getTimestamp() > timeSpan * 1000);
            DoubleSummaryStatistics stats = timeSeries.stream().collect(Collectors.summarizingDouble(Transaction::getAmount));
            average = stats.getAverage();
            count = stats.getCount();
            maximum = stats.getMax();
            minimum = stats.getMin();
            sum=stats.getSum();
        }
    }


    private Double sum= (double) 0;
    private Double average= (double) 0;
    private Double maximum= (double) 0;
    private Double minimum= (double) 0;
    private Long count= 0L;

    @Override
    public Double getSum() {
        return sum;
    }

    @Override
    public Double getAverage() {
        return Precision.round(average,2);
    }

    @Override
    public Double getMaximum() {
        return maximum;
    }

    @Override
    public Double getMinimum() {
        return minimum;
    }

    @Override
    public Long getCount() {
        return count;
    }

    @Override
    public void recordTransaction(Transaction transaction) {
        timeSeries.add(transaction);
        updateMetrics();
    }
}
