package com.example.demo.user.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class UserCreateControllerTest {

    @Test
    void 사용자는_회원_가입을_할_수_있고_회원가입된_사용자는_PENDING_상태이다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .uuidHolder(() -> "aaaa-aaaaa-aaaaaa")
                .build();

        UserCreate userCreate = UserCreate.builder()
                .email("jeongjy620@naver.com")
                .nickname("jeongjy")
                .address("seoul")
                .build();

        // when
        ResponseEntity<UserResponse> result = testContainer.userCreateController.create(userCreate);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("jeongjy620@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("jeongjy");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(testContainer.userRepository.getById(1L).getCertificationCode()).isEqualTo("aaaa-aaaaa-aaaaaa");
        assertThat(result.getBody().getLastLoginAt()).isNull();
    }
}
