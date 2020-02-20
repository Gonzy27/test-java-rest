package com.java.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class SpringBoottApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoottApplication.class, args);
    }

}
