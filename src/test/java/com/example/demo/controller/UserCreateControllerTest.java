package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class UserCreateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 사용자는_ㅊ특정_유저의_정보를_전달_받을_수_있다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/users/{id}", 1L))
            .andExpect(status().isOk());
    }
}
