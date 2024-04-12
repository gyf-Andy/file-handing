package com.gyf.filehandling;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.gyf") //看这个包下的类有没有bean注解    描到有@Component、@Controller、@Service等这些注解的类，并注册为Bean，
@MapperScan("com.gyf.mapper") //使com.gyf.mapper生成代理
public class FileHandlingApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileHandlingApplication.class, args);
    }

}
