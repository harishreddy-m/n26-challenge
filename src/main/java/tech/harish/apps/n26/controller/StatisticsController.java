package tech.harish.apps.n26.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tech.harish.apps.n26.dto.StatisticsResponse;
import tech.harish.apps.n26.service.StatisticsService;

@Api(tags="Statistics", description = "provides statistics")
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("returns the statistic based on the transactions which happened in the last 60\n" +
            "seconds.")
    public @ResponseBody StatisticsResponse get() {
        StatisticsResponse response = new StatisticsResponse.Builder()
                .sum(statisticsService.getSum())
                .average(statisticsService.getAverage())
                .maximum(statisticsService.getMaximum())
                .minimum(statisticsService.getMinimum())
                .count(statisticsService.getCount())
                .build();
        return response;
    }
}
