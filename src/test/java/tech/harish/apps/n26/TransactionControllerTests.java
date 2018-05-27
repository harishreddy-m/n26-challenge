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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tech.harish.apps.n26.util.TestUtils.getJson;

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
    public void testMissingTimestampTransaction() throws Exception {
        this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":1234.56}"))
                //.andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testFutureTimestampTransaction() throws Exception {
        this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(getJson(123.12,1627283626347L)))
                //.andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testNegativeAmountTransaction() throws Exception {
        this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(getJson(-123.12,1527283626347L)))
                //.andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testPostValidTransaction() throws Exception {
        this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(getJson(123.12,1527283626347L)))
                //.andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

}
