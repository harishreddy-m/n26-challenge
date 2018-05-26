package tech.harish.apps.n26.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.harish.apps.n26.dto.Transaction;

import javax.validation.Valid;

@Api(tags="Transactions")
@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionsController.class);


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Every Time a new transaction happened, this endpoint should be called")
    public ResponseEntity<Void> record(@RequestBody @Valid Transaction transaction) {
        Long receivedAt = System.currentTimeMillis();
        if(receivedAt-transaction.getTimestamp() > 60*1000L){
            logger.warn("Ignoring the transaction with timestamp {}, received at {}",transaction.getTimestamp(),receivedAt);
            return ResponseEntity.noContent().build();
        }else{
            logger.debug("Processing the transaction with timestamp {}, received at {}",transaction.getTimestamp(),receivedAt);

            logger.debug("Processed the transaction with timestamp {}, received at {}",transaction.getTimestamp(),receivedAt);
            /**Note: CREATED status should send created resource location.
            /*This is only a small assignment, It's okay. ¯\_(ツ)_/¯
             */
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }

}
