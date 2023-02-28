package com.springboot.blog.utils;

import java.io.IOException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import jakarta.servlet.ServletException;

public class PasswordEncoderGenerator {
	
	public static void main(String[] args) throws ServletException, IOException {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println(passwordEncoder.encode("admin"));
		
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdmkzcyIsImlhdCI6MTY3NzU2Mjg2NywiZXhwIjoxNjc4MTY3NjY2fQ.qgDU_Eh6iz-4kawPmNjJLo180uU9XBQGV5Uha3k92N5x41pxOIPlRfmy8HUlFRTb0q5V65LljmcJXeyOrP975w";
		if(StringUtils.hasText(token) ){
			System.out.println("Coming");
        }
	}

	
}
