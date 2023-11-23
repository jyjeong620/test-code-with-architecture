package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import com.example.demo.user.controller.port.CertificationService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CertificationServiceTest {

    @Test
    public void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지_테스트한다() {
        //given
        FakeMailSender mailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationServiceImpl(mailSender);

        //when
        certificationService.send("jeongjy620@gmail.com",1, "aaaa-aaaaa-aaaaaa");

        //then
        assertThat(mailSender.email).isEqualTo("jeongjy620@gmail.com");
        assertThat(mailSender.title).isEqualTo("Please certify your email address");
        assertThat(mailSender.content).isEqualTo("Please click the following link to certify your email address: http://localhost:8080/api/users/1/verify?certificationCode=aaaa-aaaaa-aaaaaa");
    }
}
