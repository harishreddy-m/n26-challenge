package tech.harish.apps.n26.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class SwaggerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnOk() throws Exception {
        this.mockMvc.perform(get("/swagger-ui.html")).andExpect(status().isOk());
    }

    @Test
    public void shouldRedirectForHome() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().is3xxRedirection());
    }
}