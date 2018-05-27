package tech.harish.apps.n26.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import tech.harish.apps.n26.util.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.harish.apps.n26.util.TestUtils.getJson;
import static tech.harish.apps.n26.util.TestUtils.makePostRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransactionsIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturn204ForOldTransaction() {
        ResponseEntity<?> actual = makePostRequest(restTemplate,port,"/transactions",getJson(12.22,1527283626347L));
        int actualStatus = actual.getStatusCodeValue();

        assertThat(actualStatus).isEqualTo(204);
        assertThat(actual.hasBody()).isFalse();
    }


    @Test
    public void shouldReturn201ForOldTransaction()  {
        ResponseEntity<?> actual = makePostRequest(restTemplate,port,"/transactions",getJson(12.22,System.currentTimeMillis()));
        int actualStatus = actual.getStatusCodeValue();

        assertThat(actualStatus).isEqualTo(201);
        assertThat(actual.hasBody()).isFalse();
    }


}
