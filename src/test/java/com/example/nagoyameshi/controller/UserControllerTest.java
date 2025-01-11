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
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Test
    public void 未ログインの場合は会員用のマイページからログインページにリダイレクトする() throws Exception {
        mockMvc.perform(get("/user"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("http://localhost/login"));
    }
	
	@Test
    @WithUserDetails("goro.inoue@example.com")
    public void ログイン済みの場合は会員のマイページが正しく表示() throws Exception {
        mockMvc.perform(get("/user"))
               .andExpect(status().isOk())
               .andExpect(view().name("user/index"));
    }
	
	@Test
    public void 未ログインの場合は会員情報ページからログインページにリダイレクトする() throws Exception {
        mockMvc.perform(get("/user/info"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("http://localhost/login"));
    }
	
	@Test
    @WithUserDetails("goro.inoue@example.com")
    public void ログイン済みの場合は会員の会員詳細ページが正しく表示() throws Exception {
        mockMvc.perform(get("/user/info"))
               .andExpect(status().isOk())
               .andExpect(view().name("user/info"));
    }
	
	@Test
    public void 未ログインの場合は会員情報編集ページからログインページにリダイレクトする() throws Exception {
        mockMvc.perform(get("/user/edit"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("http://localhost/login"));
    }
	
	@Test
    @WithUserDetails("goro.inoue@example.com")
    public void ログイン済みの場合は会員情報編集ページが正しく表示() throws Exception {
        mockMvc.perform(get("/user/edit"))
               .andExpect(status().isOk())
               .andExpect(view().name("user/edit"));
    }
}
