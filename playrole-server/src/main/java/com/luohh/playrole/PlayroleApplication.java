package com.luohh.playrole;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 */
@SpringBootApplication
@MapperScan("com.luohh.playrole.*.mapper")
public class PlayroleApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlayroleApplication.class, args);
    }

}
