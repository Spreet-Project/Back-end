package com.team1.spreet;

import com.team1.spreet.entity.User;
import com.team1.spreet.entity.UserRole;
import com.team1.spreet.security.UserDetailsImpl;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpreetApplicationTest {

    @Test
    public void asd() {
        User user = new User("123", "asd", "asd1", "d@d.com", UserRole.ROLE_USER);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        System.out.println(userDetails.getUser());
        User user1 = userDetails.getUser();
        System.out.println(user);
        System.out.println(user1);
    }
}
