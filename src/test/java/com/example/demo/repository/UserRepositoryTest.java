package com.example.demo.repository;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:test-application.properties")
@Sql("/sql/user-repository-test-data.sql")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1L, UserStatus.ACTIVE);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus_로_유저_데이터가_없으면_Optional_empty를_내려준다() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1L, UserStatus.PENDING);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("jeongjy620@gmail.com",UserStatus.ACTIVE);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus_로_유저_데이터가_없으면_Optional_empty를_내려준다() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("jeongjy620@gmail.com", UserStatus.PENDING);

        // then
        assertThat(result.isEmpty()).isTrue();
    }
}