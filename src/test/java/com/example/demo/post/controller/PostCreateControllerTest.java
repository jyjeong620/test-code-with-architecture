package com.example.demo.post.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostCreateControllerTest {

    @Test
    void 사용자는_게시물을_작성할_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 100L)
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("jeongjy620@naver.com")
                .nickname("jeongjy")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaaa-aaaaaa")
                .lastLoginAt(100L)
                .build());

        PostCreate postCreate = PostCreate.builder()
                .writerId(1L)
                .content("helloworld")
                .build();
        // when
        ResponseEntity<PostResponse> result = testContainer.postCreateController.createPost(postCreate);
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getContent()).isEqualTo("helloworld");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
        assertThat(result.getBody().getWriter().getId()).isEqualTo(1L);
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("jeongjy");

    }
}
