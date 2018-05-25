package tech.harish.apps.n26.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import tech.harish.apps.n26.dto.Transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TransactionsControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturn204ForOldTransaction() throws Exception {

        String requestJson = new ObjectMapper().writeValueAsString(ImmutableMap.builder()
                .put("amount", "12.45")
                .put("timestamp", ""+1527283626347L)
                .build());

        int actual = makePostRequest(requestJson, "/transactions/").getStatusCodeValue();

        assertThat(actual).isEqualTo(204);

    }

    private ResponseEntity<?> makePostRequest(String requestJson, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        return restTemplate.exchange(createURLWithPort(url), HttpMethod.POST, entity, Void.class);
    }


    @Test
    public void shouldReturn201ForOldTransaction() throws Exception {
        String requestJson = new ObjectMapper().writeValueAsString(ImmutableMap.builder()
                .put("amount", "12.45")
                .put("timestamp", ""+System.currentTimeMillis())
                .build());

        int actual = makePostRequest(requestJson, "/transactions").getStatusCodeValue();

        assertThat(actual).isEqualTo(201);

    }

    private String createURLWithPort(String target) {
        return "http://localhost:" + port + "/"+target;
    }
}
