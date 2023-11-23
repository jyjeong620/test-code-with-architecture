package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostResponseTest {

    @Test
    public void Post로_응답을_생성할_수_있다() {
        // given
        Post post = Post.builder()
                .id(1L)
                .content("my name is jeongjy")
                .writer(User.builder()
                        .id(1L)
                        .email("jeongjy620@naver.com")
                        .nickname("jeongjy")
                        .address("seoul")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("aaaa-aaaaa-aaaaaa")
                        .build()
                )
                .build();

        // when
        PostResponse postResponse = PostResponse.from(post);

        // then
        assertThat(postResponse.getId()).isEqualTo(1L);
        assertThat(postResponse.getContent()).isEqualTo("my name is jeongjy");
        assertThat(postResponse.getWriter().getEmail()).isEqualTo("jeongjy620@naver.com");
        assertThat(postResponse.getWriter().getNickname()).isEqualTo("jeongjy");

    }

}