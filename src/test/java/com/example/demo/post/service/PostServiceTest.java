package com.example.demo.post.service;

import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class PostServiceTest {

    private PostService postService;

    @BeforeEach
    void init() {
        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        postService  = PostService.builder()
                .userRepository(fakeUserRepository)
                .postRepository(fakePostRepository)
                .clockHolder(new TestClockHolder(1234567L))
                .build();


        User user = User.builder()
                .id(1L)
                .email("jeongjy620@gmail.com")
                .nickname("jeongjy")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaaa-aaaaaa")
                .lastLoginAt(0L)
                .build();
        fakeUserRepository.save(user);
        fakeUserRepository.save(User.builder()
                .id(2L)
                .email("jeongjy630@gmail.com")
                .nickname("jeongjy01")
                .address("seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaa-aaaaa-aaaaaa2")
                .lastLoginAt(0L)
                .build());

        fakePostRepository.save(Post.builder()
                .id(1L)
                .content("helloworld")
                .createdAt(1234567L)
                .modifiedAt(0L)
                .writer(user)
                .build());
    }

    @Test
    void getById은_존재하는_게시물을_내려준다() {
        // given

        // when
        Post result = postService.getById(1L);

        // then
        assertThat(result.getContent()).isEqualTo("helloworld");
    }

    @Test
    void PostCreateDto_를_시용하여_게시물을_등록할_수_있다() {
        PostCreate postCreate = PostCreate.builder()
                .content("my name is jeongjy")
                .writerId(1L)
                .build();

        Post result = postService.create(postCreate);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("my name is jeongjy");
        assertThat(result.getCreatedAt()).isEqualTo(1234567L);
    }

    @Test
    void PostUpdateDto_를_시용하여_게시물을_수정할_수_있다() {
        PostUpdate postUpdate = PostUpdate.builder()
                .content("my name is jeongjy620")
                .build();

        postService.update(1L, postUpdate);

        Post result = postService.getById(1L);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("my name is jeongjy620");
        assertThat(result.getCreatedAt()).isEqualTo(1234567L);
    }

}