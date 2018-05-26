package tech.harish.apps.n26.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "a statistics data of last 60 seconds")
public class StatisticsResponse {


    @ApiModelProperty(notes = "the total sum of transaction value in the last 60 seconds", example = "6123.45")
    @JsonProperty("sum")
    private Double sum;

    @ApiModelProperty(notes = "the average amount of transaction value in the last 60\n" +
            "seconds", example = "123")
    @JsonProperty("avg")
    private Double average;

    @ApiModelProperty(notes = "single highest transaction value in the last 60 seconds", example = "451.23")
    @JsonProperty("max")
    private Double maximum;

    @ApiModelProperty(notes = "single lowest transaction value in the last 60 seconds", example = "1.23")
    @JsonProperty("min")
    private Double minimum;


    @ApiModelProperty(notes = "the total number of transactions happened in the last 60\n" +
            "seconds", example = "123")
    @JsonProperty("count")
    private Long count;

    private StatisticsResponse(Builder builder) {
        sum=builder.sum;
        average=builder.average;
        maximum=builder.maximum;
        minimum=builder.minimum;
        count=builder.count;
    }

    public static class Builder {
        private Double sum;
        private Double average;
        private Double maximum;
        private Double minimum;
        private Long count;


        public Builder sum(Double sum) {
            this.sum = sum;
            return this;
        }

        public Builder average(Double average) {
            this.average = average;
            return this;
        }

        public Builder maximum(Double maximum) {
            this.maximum = maximum;
            return this;
        }

        public Builder minimum(Double minimum) {
            this.minimum = minimum;
            return this;
        }

        public Builder count(Long count) {
            this.count = count;
            return this;
        }

        public StatisticsResponse build() {
            return new StatisticsResponse(this);
        }
    }
}
