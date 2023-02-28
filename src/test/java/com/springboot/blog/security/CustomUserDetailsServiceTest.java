package com.springboot.blog.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

	@Mock
    private UserRepository userRepository;
	
	@InjectMocks
    private CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService(userRepository);
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testCustomUserDetailsService() {
		assertNotNull(new CustomUserDetailsService(userRepository));
	}

	@Test
	void testLoadUserByUsername() {
		User user = new User();
		user.setEmail("testuser@test.com");
		user.setName("Test User");
		user.setPassword("password");
		user.setUsername("testuser");
		Role role = new Role();
		role.setId(1L);
		role.setName("ADMIN");
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		user.setRoles(roles);
		Optional<User> userOptional = Optional.of(user);
		// Mock the Actual Database call
  		when(userRepository.findByUsernameOrEmail(Mockito.anyString(), Mockito.anyString())).thenReturn(userOptional);
  		UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser@test.com");
  		assertEquals("testuser@test.com", userDetails.getUsername());
	}
}