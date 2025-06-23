package com.fly.test;

import com.fly.test.entity.User;
import com.fly.test.service.PrimaryUserService;
import com.fly.test.service.SecondaryUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringbootDataSourceApplicationTests {

    @Autowired
    private PrimaryUserService primaryUserService;

    @Autowired
    private SecondaryUserService secondaryUserService;

    @Test
    public void testPrimaryDataSource() {
        List<User> users = primaryUserService.getUsers();
        System.out.println("Primary DataSource Users: " + users);
    }

    @Test
    public void testSecondaryDataSource() {
        List<User> users = secondaryUserService.getUsers();
        System.out.println("Secondary DataSource Users: " + users);
    }

}
