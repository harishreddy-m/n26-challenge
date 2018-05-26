package tech.harish.apps.n26.integration;

import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.harish.apps.n26.util.TestUtils.getJson;
import static tech.harish.apps.n26.util.TestUtils.makePostRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StatisticsIT {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsIT.class);

    private ExecutorService executor = Executors.newScheduledThreadPool(20);

    @LocalServerPort
    private int port;

    @Value("${app.series.duration:3}")
    private Long testSpan;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldIgnoreOlderData() {

        // multiple POST at T=0-3 sec
        postAndGetAfterDelay(123.45);

        // multiple POST after 3 seconds
        try {
            Thread.sleep(testSpan*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //then
        //metrics should not consider data posted between T=0-3 seconds
        postAndGetAfterDelay(678.90);

    }

    @Test
    public void testParallelReadAndWrite() {
        List<Future<ResponseEntity>> futurePost = new ArrayList<>();
        List<Future<ResponseEntity<String>>> futureGet = new ArrayList<>();

        //without this POST, asserting average is not possible
        makePostRequest(restTemplate,port,"/transactions",getJson(111.22,System.currentTimeMillis()));

        for(int i=0;i<10;i++){
            if(Math.random()>=0.5){
                Future<ResponseEntity> future = executor.submit(() -> makePostRequest(restTemplate,port,"/transactions",getJson(111.22,System.currentTimeMillis())));
                futurePost.add(future);
            }else {
                Future<ResponseEntity<String>> future =  executor.submit(() -> restTemplate.getForEntity("/statistics",String.class));
                futureGet.add(future);
            }
        }


        //asserting POST & GET responses
        try{
            for(Future<ResponseEntity> future:futurePost){
                ResponseEntity response = future.get();
                int actualStatus = response.getStatusCodeValue();
                assertThat(actualStatus).isEqualTo(201);
            }

            for(Future<ResponseEntity<String>> future:futureGet){
                ResponseEntity<String> response = future.get();
                int actualStatus = response.getStatusCodeValue();
                assertThat(actualStatus).isEqualTo(200);
                String responseJson = response.getBody();
                assertThat(responseJson).isNotEmpty();
                assertThat(JsonPath.parse(responseJson).read("$.avg",Double.class)).isEqualTo(111.22);
            }

        }catch (ExecutionException | InterruptedException e){
            logger.error(e.getLocalizedMessage());
        }

    }

    private void postAndGetAfterDelay(double amount){
        List<Future<ResponseEntity>> futurePost = new ArrayList<>();

        //multiple POST at T=0
        for(int i=0;i<10;i++){
            Future<ResponseEntity> future = executor.submit(() -> makePostRequest(restTemplate,port,"/transactions",getJson(amount,System.currentTimeMillis())));
            futurePost.add(future);
        }

        //asserting POST responses
        try{
            for(Future<ResponseEntity> future:futurePost){
                ResponseEntity response = future.get();
                int actualStatus = response.getStatusCodeValue();
                assertThat(actualStatus).isEqualTo(201);
            }

        }catch (ExecutionException | InterruptedException e){
            logger.error(e.getLocalizedMessage());
        }

        //GET request
        ResponseEntity<String> response
                = restTemplate.getForEntity("/statistics",String.class);
        int actualStatus = response.getStatusCodeValue();
        assertThat(actualStatus).isEqualTo(200);
        String responseJson = response.getBody();
        assertThat(responseJson).isNotEmpty();
        assertThat(JsonPath.parse(responseJson).read("$.avg",Double.class)).isEqualTo(amount);
        assertThat(JsonPath.parse(responseJson).read("$.sum",Double.class)).isEqualTo(amount*10);
        assertThat(JsonPath.parse(responseJson).read("$.max",Double.class)).isEqualTo(amount);
        assertThat(JsonPath.parse(responseJson).read("$.min",Double.class)).isEqualTo(amount);
        assertThat(JsonPath.parse(responseJson).read("$.count",Long.class)).isEqualTo(10);

    }
}
