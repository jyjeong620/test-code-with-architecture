package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class UserServiceImplTest {

    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository userRepository = new FakeUserRepository();

        userServiceImpl = UserServiceImpl.builder()
                .uuidHolder(new TestUuidHolder("aaaa-aaaaa-aaa"))
                .clockHolder(new TestClockHolder(12315674L))
                .userRepository(userRepository)
                .certificationService(new CertificationService(fakeMailSender))
                .build();

        userRepository.save(User.builder()
                .id(1L)
                .email("jeongjy620@gmail.com")
                .nickname("jeongjy")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaaa-aaaaaa")
                .lastLoginAt(0L)
                .build());
        userRepository.save(User.builder()
                .id(2L)
                .email("jeongjy630@gmail.com")
                .nickname("jeongjy01")
                .address("seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaa-aaaaa-aaaaaa2")
                .lastLoginAt(0L)
                .build());


    }

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        String email = "jeongjy620@gmail.com";

        // when
        User result = userServiceImpl.getByEmail(email);

        // then
        assertThat(result.getNickname()).isEqualTo("jeongjy");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저는_찾아올_수_없다() {
        // given
        String email = "jeongjy630@gmail.com";

        // when
        // then
        assertThatThrownBy(() -> {
            User result = userServiceImpl.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById는_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        // when
        User result = userServiceImpl.getById(1L);

        // then
        assertThat(result.getNickname()).isEqualTo("jeongjy");
    }

    @Test
    void getById는_PENDING_상태인_유저를_찾아올_수_없다() {
        // given
        // when
        // then
        assertThatThrownBy(() -> {
            User result = userServiceImpl.getById(2L);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto_를_이용하여_유저를_생성할_수_있다() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("jeongjy620@naver.com")
                .address("Gyeonggi")
                .nickname("jeongjy-n")
                .build();

        // when
        User result = userServiceImpl.create(userCreate);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getCertificationCode()).isEqualTo("aaaa-aaaaa-aaa");
    }

    @Test
    void userUpdateDto_를_이용하여_유저를_수정할_수_있다() {
        // given
        UserUpdate userUpdate = UserUpdate.builder()
                .address("Incheon")
                .nickname("jeongjy-n")
                .build();

        // when
        userServiceImpl.update(1L, userUpdate);

        // then
        User userEntity = userServiceImpl.getById(1L);
        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getAddress()).isEqualTo("Incheon");
        assertThat(userEntity.getNickname()).isEqualTo("jeongjy-n");
    }

    @Test
    void 유저를_로그인_시키면_마지막_로그인_시간이_변경된다() {
        // given
        // when
        userServiceImpl.login(1L);

        // then
        User userEntity = userServiceImpl.getById(1L);
        assertThat(userEntity.getLastLoginAt()).isGreaterThan(0L);
        assertThat(userEntity.getLastLoginAt()).isEqualTo(12315674L);

    }

    @Test
    void PENDING_상태의_사용자는_인증코드로_ACTIVE_시킬_수_있다() {
        // given
        // when
        userServiceImpl.verifyEmail(2L, "aaaa-aaaaa-aaaaaa2");

        // then
        User userEntity = userServiceImpl.getById(2L);
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
        // given
        // when

        // then
        assertThatThrownBy(() -> {
            userServiceImpl.verifyEmail(2L, "aaaa-aaaaa-aaaaaa");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}