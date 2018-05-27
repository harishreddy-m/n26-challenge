package tech.harish.apps.n26.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@ApiModel(description = "a transaction event")
/*
 * No setters are included to make the class immutable
 */
public class Transaction {

    @ApiModelProperty(notes = "transaction amount", example = "12.3", required = true)
    @NotNull
    @PositiveOrZero(message = "Amount cannot be negative")
    private Double amount;

    @ApiModelProperty(notes = "transaction time in epoch in millis in UTC time zone", example = "1478192204000", required = true)
    @NotNull
    @PastTimestamp(message="Invalid timestamp.Timestamp is from future")
    private Long timestamp;

    @JsonCreator
    public Transaction(@JsonProperty("amount") Double amount, @JsonProperty("timestamp") Long timestamp) {
    this.amount=amount;
    this.timestamp=timestamp;
    }

    public Double getAmount() {
        return amount;
    }

    public Long getTimestamp() {
        return timestamp;
    }

}
