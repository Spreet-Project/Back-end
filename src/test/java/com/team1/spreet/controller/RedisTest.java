package com.team1.spreet.controller;

import com.team1.spreet.entity.EmailConfirm;
import com.team1.spreet.repository.EmailConfirmRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisTest {
    @Autowired
    EmailConfirmRepository emailConfirmRepository;

    //insert
    @Test
    void saveTest(){
        EmailConfirm email = new EmailConfirm("email", "zzarbttoo1");

        emailConfirmRepository.save(email);
        System.out.println("done");
    }

//    //select
//    @Test
//    void selectTest(){
//
//
//        Student selectStudent1 = studentRepository.findById("1").get();
//        Student selectStudent2 = studentRepository.findById("2").get();
//        Student selectStudent3 = studentRepository.findById("3").get();
//        Student selectStudent4 = studentRepository.findById("4").get();
//        Student selectStudent5 = studentRepository.findById("5").get();
//
//        System.out.println(selectStudent1.toString());
//        System.out.println(selectStudent2.toString());
//        System.out.println(selectStudent3.toString());
//        System.out.println(selectStudent4.toString());
//        System.out.println(selectStudent5.toString());
//    }
//
//    //update
//    @Test
//    void updateTest(){
//
//        Student selectStudent1 = studentRepository.findById("1").get();
//        selectStudent1.setName("updated Name");
//        studentRepository.save(selectStudent1);
//        System.out.println(selectStudent1.toString());
//
//    }
//
//    //delete
//    @Test
//    void deleteTest(){
//        studentRepository.deleteById("2");
//        Assertions.assertThrows(NoSuchElementException.class, () -> studentRepository.findById("2").get());
//    }
//
//    //selectAll
//    @Test
//    void selectAllTest(){
//        List<Student> students = new ArrayList<>();
//        studentRepository.findAll().forEach(students::add);
//        System.out.println(students.toString());
//    }
}
