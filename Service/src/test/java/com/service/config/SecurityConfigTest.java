package com.service.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.accessor.StudentAccessor;
import com.example.data.Admin;
import com.example.data.AdminRepository;
import com.example.data.Student;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

	 @Mock
	    private AdminRepository adminRepository;

	    @Mock
	    private StudentAccessor studentAccessor;

	    @InjectMocks
	    private SecurityConfig securityConfig;

	    private UserDetailsService userDetailsService;

	    @BeforeEach
	    void setUp() {
	        userDetailsService = securityConfig.userDetailsService();
	    }

	    @Test
	    void testLoadAdminUserDetails_Success() {
	        // Arrange
	        String username = "admin123";
	        Admin admin = new Admin();
	        admin.setUsername(username);
	        admin.setPassword(new BCryptPasswordEncoder().encode("password")); // assuming password is encoded
	        
	        when(adminRepository.findByUsername(username)).thenReturn(admin);

	        // Act
	        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

	        // Assert
	        assertNotNull(userDetails);
	        assertEquals(username, userDetails.getUsername());
	        assertTrue(userDetails.getAuthorities().stream()
	                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")));
	    }

	    @Test
	    void testLoadStudentUserDetails_Success() {
	        // Arrange
	        String username = "student@example.com";
	        Student student = new Student();
	        student.setEmail(username);
	        student.setPassword(new BCryptPasswordEncoder().encode("password")); // assuming password is encoded
	        student.setIsActive(0); // Active student

	        when(studentAccessor.findByEmail(username)).thenReturn(student);

	        // Act
	        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

	        // Assert
	        assertNotNull(userDetails);
	        assertEquals(username, userDetails.getUsername());
	        assertTrue(userDetails.getAuthorities().stream()
	                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_STUDENT")));
	    }

	    @Test
	    void testLoadAdminUserDetails_NotFound() {
	        // Arrange
	        String username = "admin123";
	        when(adminRepository.findByUsername(username)).thenReturn(null);

	        // Act & Assert
	        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
	            userDetailsService.loadUserByUsername(username);
	        });
	        assertEquals("Admin not found", exception.getMessage());
	    }

	    @Test
	    void testLoadStudentUserDetails_NotFound() {
	        // Arrange
	        String username = "student@example.com";
	        when(studentAccessor.findByEmail(username)).thenReturn(null);

	        // Act & Assert
	        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
	            userDetailsService.loadUserByUsername(username);
	        });
	        assertEquals("User not found", exception.getMessage());
	    }

	    @Test
	    void testLoadInactiveStudentUserDetails() {
	        // Arrange
	        String username = "inactive@example.com";
	        Student student = new Student();
	        student.setEmail(username);
	        student.setPassword(new BCryptPasswordEncoder().encode("password")); // assuming password is encoded
	        student.setIsActive(1); // Inactive student

	        when(studentAccessor.findByEmail(username)).thenReturn(student);

	        // Act & Assert
	        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
	            userDetailsService.loadUserByUsername(username);
	        });
	        assertEquals("User not found", exception.getMessage());
	    }

}
