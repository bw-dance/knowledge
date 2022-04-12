package com.example.pachong;

import com.example.pachong.csdn.CsdnController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class PachongApplicationTests {
    @Autowired
    CsdnController csdnController;

    @Test
    void contextLoads() throws IOException {
        csdnController.getArticleByUrl("");
    }

}
