package com.springboot.blog.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

	@Mock
    private Authentication authentication;
	
	@InjectMocks
    private JwtTokenProvider jwtTokenProvider;
    
	@BeforeEach
	void setUp() throws Exception {
		ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "JWTSecretKey1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
		ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", 604800000);
	}

	@Test
	void testGenerateToken() {
		when(authentication.getName()).thenReturn("avi3s");
		String token = jwtTokenProvider.generateToken(authentication);
		assertNotNull(token);
	}

	@Test
	void testGetUsernameFromJWT() {
		when(authentication.getName()).thenReturn("avi3s");
		String username = jwtTokenProvider.getUsernameFromJWT(jwtTokenProvider.generateToken(authentication));
		assertNotNull(username);
	}

	@Test
	void testValidateToken() {
		when(authentication.getName()).thenReturn("avi3s");
		assertTrue(jwtTokenProvider.validateToken(jwtTokenProvider.generateToken(authentication)));
	}
}