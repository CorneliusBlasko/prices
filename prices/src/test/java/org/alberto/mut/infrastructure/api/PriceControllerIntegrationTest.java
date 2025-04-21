package org.alberto.mut.infrastructure.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
public class PriceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetPrice1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/prices")
                        .param("application_date", "2020-06-14T10:00:00")
                        .param("product_id", "35455")
                        .param("brand_id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(35.50));
    }

    @Test
    void testGetPrice2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/prices")
                        .param("application_date", "2020-06-14T16:00:00")
                        .param("product_id", "35455")
                        .param("brand_id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(25.45));
    }

    @Test
    void testGetPrice3() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/prices")
                        .param("application_date", "2020-06-14T21:00:00")
                        .param("product_id", "35455")
                        .param("brand_id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(35.50));
    }

    @Test
    void testGetPrice4() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/prices")
                        .param("application_date", "2020-06-15T10:00:00")
                        .param("product_id", "35455")
                        .param("brand_id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(30.50));
    }

    @Test
    void testGetPrice5() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/prices")
                        .param("application_date", "2020-06-16T21:00:00")
                        .param("product_id", "35455")
                        .param("brand_id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(38.95));
    }

    @Test
    void testGetPrice_defaultPrice() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/prices")
                        .param("application_date", "2024-06-13T09:00:00")
                        .param("product_id", "35455")
                        .param("brand_id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.defaultPrice").value(40.00));
    }

    @Test
    void testGetPrice_notFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/prices")
                        .param("application_date", "2020-06-13T09:00:00")
                        .param("product_id", "999")
                        .param("brand_id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

