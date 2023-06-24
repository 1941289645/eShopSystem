package com.example.springboot;

import com.example.springboot.service.IOrdersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class SpringbootApplicationTests {

    @Autowired
    private IOrdersService ordersService;


    @Test
    void contextLoads() {
//        int [] test = {15,17};
//        ordersService.removeByIds()
    }



}
