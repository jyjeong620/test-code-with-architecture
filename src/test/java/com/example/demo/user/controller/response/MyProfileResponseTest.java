package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MyProfileResponseTest {

    @Test
    void User로_응답을_생성할_수_있다() {
        // given
        User user = User.builder()
                .email("jeongjy620@naver.com")
                .nickname("jeongjy")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaaa-aaaaaa")
                .lastLoginAt(1000L)
                .build();

        // when
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

        // then
        assertThat(myProfileResponse.getEmail()).isEqualTo("jeongjy620@naver.com");
        assertThat(myProfileResponse.getNickname()).isEqualTo("jeongjy");
        assertThat(myProfileResponse.getAddress()).isEqualTo("seoul");
        assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(1000L);
    }

}