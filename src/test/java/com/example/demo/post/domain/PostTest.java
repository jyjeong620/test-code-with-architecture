package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    public void PostCreate으로_게시물을_만들_수_있다() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .content("my name is jeongjy")
                .writerId(1L)
                .build();
        User writer = User.builder()
                .email("jeongjy620@naver.com")
                .nickname("jeongjy")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaaa-aaaaaa")
                .build();

        // when
        Post post = Post.from(writer, postCreate, new TestClockHolder(1234567L));

        // then
        assertThat(post.getContent()).isEqualTo("my name is jeongjy");
        assertThat(post.getCreatedAt()).isEqualTo(1234567L);
        assertThat(post.getWriter().getEmail()).isEqualTo("jeongjy620@naver.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("jeongjy");
        assertThat(post.getWriter().getAddress()).isEqualTo("seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaa-aaaaa-aaaaaa");
    }

    @Test
    public void PostUpdate로_게시물을_수정할_수_있다() {
        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("my name is jeongjy")
                .build();
        User writer = User.builder()
                .email("jeongjy620@naver.com")
                .nickname("jeongjy")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaaa-aaaaaa")
                .build();
        Post post = Post.builder()
                .content("my name is jeongjy")
                .createdAt(1234567L)
                .writer(writer)
                .build();

        // when
        post = post.update(postUpdate, new TestClockHolder(21234567L));

        // then
        assertThat(post.getContent()).isEqualTo("my name is jeongjy");
        assertThat(post.getCreatedAt()).isEqualTo(1234567L);
        assertThat(post.getModifiedAt()).isEqualTo(21234567L);
        assertThat(post.getWriter().getEmail()).isEqualTo("jeongjy620@naver.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("jeongjy");
        assertThat(post.getWriter().getAddress()).isEqualTo("seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaa-aaaaa-aaaaaa");
    }

}