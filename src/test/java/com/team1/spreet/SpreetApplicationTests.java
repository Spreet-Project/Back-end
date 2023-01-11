package com.team1.spreet;

import com.team1.spreet.entity.EmailConfirm;
import com.team1.spreet.repository.EmailConfirmRepository;
import com.team1.spreet.service.EmailConfirmService;
import com.team1.spreet.service.implement.EmailServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SpreetApplicationTests {

    EmailConfirmService emailConfirmService;
    RedissonClient redissonClient;
    EmailConfirmRepository emailConfirmRepository;

    EmailServiceImpl emailService;

    @Autowired
    public void RedissonTest(EmailConfirmService emailConfirmService, RedissonClient redissonClient, EmailConfirmRepository emailConfirmRepository) {
        this.emailConfirmService = emailConfirmService;
        this.redissonClient = redissonClient;
        this.emailConfirmRepository = emailConfirmRepository;
    }


    @Test
    @Order(1)
    void saveTest(){
        EmailConfirm emailConfirm1 = new EmailConfirm("email.com","confirmCode");

        emailConfirmService.save(emailConfirm1);
    }

    @Test
    @Order(2)
    void selectTest(){

        long start1 = System.currentTimeMillis();

        System.out.println(emailConfirmService.findByEmail("1").toString());
//        System.out.println(emailConfirmService.findById("2").toString());
//        System.out.println(emailConfirmService.findById("3").toString());
//        System.out.println(emailConfirmService.findById("4").toString());
//        System.out.println(emailConfirmService.findById("5").toString());

        long end1 = System.currentTimeMillis();
        System.out.println(end1 - start1);

        long start2 = System.currentTimeMillis();

        System.out.println(emailConfirmService.findByEmail("1").toString());
//        System.out.println(emailConfirmService.findById("2").toString());
//        System.out.println(emailConfirmService.findById("3").toString());
//        System.out.println(emailConfirmService.findById("4").toString());
//        System.out.println(emailConfirmService.findById("5").toString());

        long end2 = System.currentTimeMillis();

        System.out.println(end2 - start2);

        long start3 = System.currentTimeMillis();

        System.out.println(emailConfirmService.findByEmail("1").toString());
        System.out.println(emailConfirmService.findByEmail("2").toString());
        System.out.println(emailConfirmService.findByEmail("3").toString());
        System.out.println(emailConfirmService.findByEmail("4").toString());
        System.out.println(emailConfirmService.findByEmail("5").toString());

        long end3 = System.currentTimeMillis();

        System.out.println(end3 - start3);


    }

    @Test
    @Order(3)
    void updateTest() throws Exception {

        emailConfirmService.update("1", "updated Name");

        EmailConfirm selectEmailConfirm = emailConfirmRepository.findById("1").get();
        System.out.println(selectEmailConfirm.toString());

        EmailConfirm redisEmailConfirm = emailConfirmService.findByEmail("1");
        System.out.println(redisEmailConfirm.toString());

    }

    @Test
    @Order(4)
    void deleteTest(){

        System.out.println(emailConfirmService.findByEmail("4"));
        emailConfirmService.delete("4");
        Assertions.assertNull(emailConfirmService.findByEmail("4"));
    }
}
