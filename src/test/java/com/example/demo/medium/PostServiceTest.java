package com.example.demo.medium;

import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@TestPropertySource(locations = "classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/post-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class PostServiceTest {


    @Autowired
    private PostService postService;

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
        assertThat(result.getCreatedAt()).isGreaterThan(0L);
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
        assertThat(result.getModifiedAt()).isGreaterThan(0L);
    }

}