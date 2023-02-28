package com.springboot.blog.security;

import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

	@Mock
    private JwtTokenProvider tokenProvider;
	
	@Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private FilterChain filterChain;

	@Mock
    private CustomUserDetailsService customUserDetailsService;
	
	@InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Mock
    private Authentication authentication;
	
	@Mock
    private JwtTokenProvider jwtTokenProvider;
    
	@BeforeEach
	void setUp() throws Exception {
		ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "JWTSecretKey1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
		ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", 604800000);
	}

	@Test
	void testDoFilterInternalHttpServletRequestHttpServletResponseFilterChain() {
		
		String token = jwtTokenProvider.generateToken(authentication);
		token = "Bearer "+token;
		when(request.getHeader("Authorization")).thenReturn(token);
		try {
			jwtAuthenticationFilter.doFilter(request, response, filterChain);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}
}