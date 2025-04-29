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
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateAccount() throws Exception {
        String json = "{\"name\": \"John Doe\", \"accountType\": \"Saving\"}";

        mockMvc.perform(post("/createAccount")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateAccount() throws Exception {
        String json = "{\"accountType\": \"Current\"}";

        mockMvc.perform(put("/updateAccount/1")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testViewPolicy() throws Exception {
        mockMvc.perform(get("/viewPolicy/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeletePolicy() throws Exception {
        mockMvc.perform(delete("/deletePolicy/1"))
                .andExpect(status().isOk());
    }
}
