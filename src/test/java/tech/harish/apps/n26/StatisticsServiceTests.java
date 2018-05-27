package tech.harish.apps.n26;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import tech.harish.apps.n26.dto.Transaction;
import tech.harish.apps.n26.service.StatisticsService;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StatisticsServiceTests {

    /*
     * Using time span of 3 seconds for testing "app.series.duration" property
     */
    @Value("${app.series.duration}")
    private int testSpan;

    @Autowired
    private StatisticsService inmemoryService;

    @Test
    public void shouldInsertTransaction(){

        //when
        Transaction testData= new Transaction(12.22,System.currentTimeMillis());

        inmemoryService.recordTransaction(testData);

        //then
        Assert.assertNotNull(inmemoryService.getSum());
        Assert.assertNotNull(inmemoryService.getAverage());
        Assert.assertNotNull(inmemoryService.getMaximum());
        Assert.assertNotNull(inmemoryService.getMinimum());
        Assert.assertNotNull(inmemoryService.getCount());
    }

    @Test
    public void shouldInsertParallelTransaction(){

        //when
        ExecutorService executor = Executors.newFixedThreadPool(12);
        for(int i=0;i<10;i++){
            executor.submit(() -> {
                Transaction randomData= new Transaction(10.23,System.currentTimeMillis()- new Random().nextInt(testSpan*1000));
                inmemoryService.recordTransaction(randomData);
            });
        }

        executor.submit(() -> {
            Transaction randomData= new Transaction(34.56,System.currentTimeMillis()- new Random().nextInt(testSpan*1000));
            inmemoryService.recordTransaction(randomData);
        });

        //Since this transaction is older than 3 seconds, metrics calculation should not consider this
        executor.submit(() -> {
            Transaction randomData= new Transaction(3.14,System.currentTimeMillis()- (testSpan + 1) * 1000);
            inmemoryService.recordTransaction(randomData);
        });

        executor.shutdown();

        //For consistent metric calculation, stopping this thread for 100 milliseconds
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //then
        Assert.assertNotNull(inmemoryService.getSum());
        Assert.assertNotNull(inmemoryService.getAverage());
        Assert.assertNotNull(inmemoryService.getMaximum());
        Assert.assertNotNull(inmemoryService.getMinimum());
        Assert.assertNotNull(inmemoryService.getCount());


        assertThat(inmemoryService.getSum()).isEqualTo(136.86);
        assertThat(inmemoryService.getCount()).isEqualTo(11);
        assertThat(inmemoryService.getAverage()).isEqualTo(12.44);
        assertThat(inmemoryService.getMaximum()).isEqualTo(34.56);
        assertThat(inmemoryService.getMinimum()).isEqualTo(10.23);
    }
}
