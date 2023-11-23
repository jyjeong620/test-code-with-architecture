package com.example.demo.post.controller;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PostControllerTest {

    @Test
    void 사용자는_게시물을_단건_조회할_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 100L)
                .build();
        User user = User.builder()
                .id(1L)
                .email("jeongjy620@naver.com")
                .nickname("jeongjy")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaaa-aaaaaa")
                .lastLoginAt(100L)
                .build();
        testContainer.userRepository.save(user);
        testContainer.postRepository.save(Post.builder()
                .id(1L)
                .content("helloworld")
                .createdAt(100L)
                .writer(user)
                .build());

        // when
        ResponseEntity<PostResponse> result = testContainer.postController.getById(1L);
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getContent()).isEqualTo("helloworld");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
        assertThat(result.getBody().getWriter().getId()).isEqualTo(1L);
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("jeongjy");

    }

    @Test
    void 존재하지_않는_게시물_정보를_조회할_경우_404_에러를_응답한다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 100L)
                .build();


        // when
        // then
        assertThatThrownBy(() ->
                testContainer.postController.getById(2L)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 게시물을_수정할_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 123L)
                .build();
        User user = User.builder()
                .id(1L)
                .email("jeongjy620@naver.com")
                .nickname("jeongjy")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaaa-aaaaaa")
                .lastLoginAt(100L)
                .build();
        testContainer.userRepository.save(user);
        testContainer.postRepository.save(Post.builder()
                .id(1L)
                .content("helloworld")
                .createdAt(100L)
                .writer(user)
                .build());

        PostUpdate postUpdate = PostUpdate.builder()
                .content("my name is jeongjy")
                .build();

        // when
        ResponseEntity<PostResponse> result = testContainer.postController.update(1L, postUpdate);
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getContent()).isEqualTo("my name is jeongjy");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
        assertThat(result.getBody().getModifiedAt()).isEqualTo(123L);
        assertThat(result.getBody().getWriter().getId()).isEqualTo(1L);
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("jeongjy");
    }

}
