package com.sys.esignature;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class EsignatureApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsignatureApplication.class, args);
    }

    @Component
    public static class MyCommandLineRunner implements CommandLineRunner {

        @Override
        public void run(String... args) throws Exception {
            System.out.println("系统启动成功！");
        }
    }

}
