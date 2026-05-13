package com.ruangong.heritage;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPassword {

    //BCryptPasswordEncoder 是spring Securty 这个框架中的工具类
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        String encode = passwordEncoder.encode("123456");
        System.out.println(encode);

        // $2a$12$BQx.PlQcNgxfwc4qyQSy1O1niJ5eWnDCNWJXqBl0U3BSorWamx3E2
        boolean matches = passwordEncoder.matches("123456", "$2a$12$BQx.PlQcNgxfwc4qyQSy1O1niJ5eWnDCNWJXqBl0U3BSorWamx3E2");
        System.out.println(matches);

    }
}
