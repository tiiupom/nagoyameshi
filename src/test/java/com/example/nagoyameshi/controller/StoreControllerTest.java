package com.example.nagoyameshi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StoreControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Test
    public void 未ログインの場合は会員用の店舗一覧ページが正しく表示される() throws Exception {
        mockMvc.perform(get("/stores"))
               .andExpect(status().isOk())
               .andExpect(view().name("stores/index"));
    }

    @Test
    @WithUserDetails("goro.inoue@example.com")
    public void ログイン済みの場合は会員用の店舗一覧ページが正しく表示される() throws Exception {
        mockMvc.perform(get("/stores"))
               .andExpect(status().isOk())
               .andExpect(view().name("stores/index"));
    }
}
