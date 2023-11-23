package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class UserControllerTest {

    @Test
    void 사용자는_특정_유저의_정보를_전달_받을_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("jeongjy620@naver.com")
                .nickname("jeongjy")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaaa-aaaaaa")
                .build());

        // when
        ResponseEntity<UserResponse> result = UserController.builder()
                .userReadService(testContainer.userReadService)
                .build()
                .getUserById(1L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("jeongjy620@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("jeongjy");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);

    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_api_호출할_경우_404_응답을_받는다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder().build();
        // when
        // then
        assertThatThrownBy(() -> UserController.builder()
                .userReadService(testContainer.userReadService)
                .build()
                .getUserById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화할_수_있다() {
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("jeongjy620@naver.com")
                .nickname("jeongjy")
                .address("seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaa-aaaaa-aaaaaa")
                .build());

        // when
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(1L, "aaaa-aaaaa-aaaaaa");

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(testContainer.userRepository.getById(1L).getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_인증_코드가_일치하지_않을_경우_권한_없음_에러를_내려준다() throws Exception {
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("jeongjy620@naver.com")
                .nickname("jeongjy")
                .address("seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaa-aaaaa-aaaaaa")
                .build());

        // when
        // then
        assertThatThrownBy(() ->
                testContainer.userController.verifyEmail(1L, "aaaa-aaaaa-aaaaaa22"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);

    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() throws Exception {
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 123456L)
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

        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("jeongjy620@naver.com");

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("jeongjy620@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("jeongjy");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getAddress()).isEqualTo("seoul");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(123456L);

    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다()  {
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("jeongjy620@naver.com")
                .nickname("jeongjy")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaaa-aaaaaa")
                .lastLoginAt(100L)
                .build());

        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("jeongjy2")
                .address("Gyeonggi")
                .build();

        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo("jeongjy620@naver.com",userUpdate);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("jeongjy620@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("jeongjy2");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getAddress()).isEqualTo("Gyeonggi");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
    }
}
