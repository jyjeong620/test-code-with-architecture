package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {

    @Test
    public void UserCreate_객체로_생성할_수_있다() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("jeongjy620@naver.com")
                .nickname("jeongjy")
                .address("Seoul")
                .build();

        // when
        User user = User.from(userCreate, new TestUuidHolder("aaaa-aaaaa-aaa"));

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("jeongjy620@naver.com");
        assertThat(user.getNickname()).isEqualTo("jeongjy");
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaa-aaaaa-aaa");

    }
    @Test
    public void UserUpdate_객체로_업데이트_할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("jeongjy620@naver.com")
                .nickname("jeongjy")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaaa-aaaaaa")
                .build();

        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("jeongjy-t")
                .address("PanGyo")
                .build();
        // when
        user = user.update(userUpdate);

        // then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("jeongjy620@naver.com");
        assertThat(user.getNickname()).isEqualTo("jeongjy-t");
        assertThat(user.getAddress()).isEqualTo("PanGyo");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getCertificationCode()).isEqualTo("aaaa-aaaaa-aaaaaa");

    }

    @Test
    public void 로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("jeongjy620@naver.com")
                .nickname("jeongjy")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaaa-aaaaaa")
                .build();


        // when
        user = user.login(new TestClockHolder(12341567891526489L));

        // then
        assertThat(user.getLastLoginAt()).isEqualTo(12341567891526489L);
    }

    @Test
    public void 유효한_인증_코드로_계정을_활성화_할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("jeongjy620@naver.com")
                .nickname("jeongjy")
                .address("seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaa-aaaaa-aaaaaa")
                .build();

        // when
        user = user.certificate("aaaa-aaaaa-aaaaaa");
        // then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void 잘못된_인증_코드로_계정을_활성화_하려면_에러를_던진다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("jeongjy620@naver.com")
                .nickname("jeongjy")
                .address("seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaa-aaaaa-aaaaaa")
                .build();

        // when
        // then
        assertThatThrownBy(() -> user.certificate("aaaa-aaaaa-aaaaaa2"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}
