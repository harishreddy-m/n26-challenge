package tech.harish.apps.n26;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import tech.harish.apps.n26.controller.StatisticsController;
import tech.harish.apps.n26.service.StatisticsService;
import tech.harish.apps.n26.util.TestDataGenerator;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsService statisticsService;


    @Test
    public void testNoDataGetRequest() throws Exception {
        this.mockMvc.perform(get("/statistics").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").exists())
                .andExpect(jsonPath("$.avg").exists())
                .andExpect(jsonPath("$.max").exists())
                .andExpect(jsonPath("$.min").exists())
                .andExpect(jsonPath("$.count").exists());
    }

    @Test
    public void testWithDataGetRequest() throws Exception {
        when(statisticsService.getAverage()).thenReturn(12.04);
        when(statisticsService.getSum()).thenReturn(132.34);
        when(statisticsService.getMaximum()).thenReturn(82.80);
        when(statisticsService.getMinimum()).thenReturn(2.62);
        when(statisticsService.getCount()).thenReturn(124L);

        this.mockMvc.perform(get("/statistics").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").exists())
                .andExpect(jsonPath("$.avg").exists())
                .andExpect(jsonPath("$.max").exists())
                .andExpect(jsonPath("$.min").exists())
                .andExpect(jsonPath("$.count").exists())
                .andExpect(jsonPath("$.sum").value(132.34))
                .andExpect(jsonPath("$.avg").value(12.04))
                .andExpect(jsonPath("$.max").value(82.80))
                .andExpect(jsonPath("$.min").value(2.62))
                .andExpect(jsonPath("$.count").value(124));
    }

}
