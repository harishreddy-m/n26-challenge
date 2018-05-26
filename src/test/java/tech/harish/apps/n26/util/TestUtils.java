package tech.harish.apps.n26.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

public class TestUtils {

    private static final Logger logger = LoggerFactory.getLogger(TestUtils.class);


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

    public static ResponseEntity<?> makePostRequest(TestRestTemplate testRestTemplate, int port, String url, String requestJson ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        return testRestTemplate.exchange(createURLWithPort(port,url), HttpMethod.POST, entity, Void.class);
    }


    private static String createURLWithPort(int port,String target) {
        return "http://localhost:" + port + "/"+target;
    }
}
