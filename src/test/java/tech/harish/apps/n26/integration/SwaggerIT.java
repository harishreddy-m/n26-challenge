package tech.harish.apps.n26.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SwaggerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnOk() {

        ResponseEntity<String> response
                = restTemplate.getForEntity("/swagger-ui.html",String.class);
        int actualStatus = response.getStatusCodeValue();

        assertThat(actualStatus).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
    }

    @Test
    public void shouldRedirectForHome() {
        ResponseEntity<String> response
                = restTemplate.getForEntity("/",String.class);
        int actualStatus = response.getStatusCodeValue();

        assertThat(actualStatus).isEqualTo(200);
        assertThat(response.hasBody()).isTrue();
    }
}