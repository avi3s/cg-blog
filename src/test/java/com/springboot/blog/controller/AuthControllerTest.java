package com.springboot.blog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.payload.JWTAuthResponse;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.SignUpDto;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthenticateUser() {
        LoginDto loginDto = new LoginDto();
        loginDto.setPassword("password");
        loginDto.setUsernameOrEmail("testuser@test.com");
        
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        when(tokenProvider.generateToken(authentication)).thenReturn("test-token");

        ResponseEntity<JWTAuthResponse> response = authController.authenticateUser(loginDto);

        assertEquals("test-token", response.getBody().getAccessToken());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRegisterUser() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setEmail("testuser@test.com");
        signUpDto.setName("Test User");
        signUpDto.setPassword("password");
        signUpDto.setUsername("testuser");
        
        when(userRepository.existsByUsername(signUpDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpDto.getEmail())).thenReturn(false);

        Role role = new Role();
        role.setName("ROLE_ADMIN");
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(role));

        when(passwordEncoder.encode(signUpDto.getPassword())).thenReturn("hashed-password");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName(signUpDto.getName());
        savedUser.setUsername(signUpDto.getUsername());
        savedUser.setEmail(signUpDto.getEmail());
        savedUser.setPassword("hashed-password");
        savedUser.setRoles(Collections.singleton(role));

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        ResponseEntity<?> response = authController.registerUser(signUpDto);

        assertEquals("User registered successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(userRepository, times(1)).save(any(User.class));
    }
}
