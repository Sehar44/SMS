package com.service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class LoginControllerTest {

    private LoginController loginController;

    @BeforeEach
    void setUp() {
        loginController = new LoginController();
    }

    @Test
    void testShowLoginPage() {
        String result = loginController.showLoginPage();
        assertEquals("login", result);  // Check if it returns the correct view name
    }

    @Test
    void testDefaultAfterLoginAsAdmin() {
        // Arrange
        SecurityContext securityContext = new SecurityContext() {
            @Override
            public Authentication getAuthentication() {
                return new Authentication() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        Collection<GrantedAuthority> authorities = new ArrayList<>();
                        authorities.add(() -> "ROLE_ADMIN"); // Use a lambda for GrantedAuthority
                        return authorities;
                    }

                    @Override
                    public Object getCredentials() { return null; }
                    @Override
                    public Object getDetails() { return null; }
                    @Override
                    public Object getPrincipal() { return null; }
                    @Override
                    public boolean isAuthenticated() { return true; }
                    @Override
                    public void setAuthenticated(boolean isAuthenticated) {
                    	// This method is intentionally left unimplemented because this test case
                        // does not require changing the authentication state. If this method is called,
                        // it will throw an exception to indicate that setting authenticated status is unsupported.
                        throw new UnsupportedOperationException("Setting authenticated status is not supported in this context.");
                    }
                    @Override
                    public String getName() { return "admin"; }
                };
            }

            @Override
            public void setAuthentication(Authentication authentication) {
            	// This method is intentionally left unimplemented because this test
                // does not require setting the authentication. If this method is called,
                // it will throw an exception to indicate that setting authentication is unsupported.
                throw new UnsupportedOperationException("Setting authentication is not supported in this context.");
            }
        };

        SecurityContextHolder.setContext(securityContext);

        // Act
        String result = loginController.defaultAfterLogin();

        // Assert
        assertEquals("redirect:/admin/home", result);  // Check if it redirects to admin home
    }


    @Test
    void testDefaultAfterLoginAsNonAdmin() {
        // Arrange
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getAuthorities()).thenReturn(Collections.emptyList());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        String result = loginController.defaultAfterLogin();

        // Assert
        assertEquals("redirect:/students/studentHome", result);  // Check if it redirects to student home
    }
}
