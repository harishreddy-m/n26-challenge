package tech.harish.apps.n26.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDataGenerator {

    private static final Logger logger = LoggerFactory.getLogger(TestDataGenerator.class);


    public static String getJson(double amount,long timestamp){
        String requestJson = null;
        try {
            requestJson = new ObjectMapper().writeValueAsString(ImmutableMap.builder()
                    .put("amount", amount)
                    .put("timestamp", timestamp)
                    .build());
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return requestJson;
    }
}
