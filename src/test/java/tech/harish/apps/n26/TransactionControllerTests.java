package tech.harish.apps.n26;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import tech.harish.apps.n26.controller.TransactionsController;
import tech.harish.apps.n26.service.StatisticsService;
import tech.harish.apps.n26.util.TestDataGenerator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionsController.class)
public class TransactionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsService statisticsService;


    @Test
    public void testPostInvalidJsonTransaction() throws Exception {
        this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content("{amount:123.23}"))
                //.andDo(print())
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void testPostInvalidTransaction() throws Exception {
        this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":123.23}"))
                //.andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testPostValidTransaction() throws Exception {
        this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(TestDataGenerator.getJson(123.12,1527283626347L)))
                //.andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

}
