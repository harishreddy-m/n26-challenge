package tech.harish.apps.n26.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.harish.apps.n26.dto.Transaction;
import tech.harish.apps.n26.service.StatisticsService;

import javax.validation.Valid;

@Api(tags="Transactions",description = "handles transactions")
@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionsController.class);

    @Value("${app.series.duration:60}")
    private Long timeSpan;

    @Autowired
    private StatisticsService statisticsService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Every Time a new transaction happened, this endpoint should be called")
    public ResponseEntity<Void> record(@RequestBody @Valid Transaction transaction) {
        Long receivedAt = System.currentTimeMillis();
        if(receivedAt-transaction.getTimestamp() > timeSpan*1000L){
            logger.warn("Ignoring the transaction with timestamp {}, received at {}",transaction.getTimestamp(),receivedAt);
            return ResponseEntity.noContent().build();
        }else{
            logger.debug("Processing the transaction with timestamp {}, received at {}",transaction.getTimestamp(),receivedAt);
            statisticsService.recordTransaction(transaction);
            logger.debug("Processed the transaction with timestamp {}, received at {}",transaction.getTimestamp(),receivedAt);
            /*Note: CREATED status should send created resource location.
             *This is only a small assignment, It's okay. ¯\_(ツ)_/¯
             */
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }

}
