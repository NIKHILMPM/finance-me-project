package com.project.staragile.banking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BankingApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        // Basic test to ensure Spring context loads
    }

    @Test
    void testCreateAccount() throws Exception {
        String json = "{\"accountNumber\": 2, \"accountName\": \"John Doe\", \"accountType\": \"Saving\", \"accountBalance\": 10000.0}";

        mockMvc.perform(post("/createAccount")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateAccount() throws Exception {
        String json = "{\"accountType\": \"Current\"}";

        mockMvc.perform(put("/updateAccount/1")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testViewPolicy() throws Exception {
        mockMvc.perform(get("/viewPolicy/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletePolicy() throws Exception {
        mockMvc.perform(delete("/deletePolicy/1"))
                .andExpect(status().isOk());
    }
}
