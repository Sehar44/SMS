package com.example.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AdminTest {
	@Test
   void testDefaultConstructor() {
        // Arrange & Act
        Admin admin = new Admin();
        
        // Assert
        assertEquals(null, admin.getUsername());
        assertEquals(null, admin.getPassword());
        assertEquals("ADMIN", admin.getRole());
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        String username = "adminUser";
        String password = "adminPass";
        String role = "ADMIN";
        
        // Act
        Admin admin = new Admin(username, password, role);
        
        // Assert
        assertEquals(username, admin.getUsername());
        assertEquals(password, admin.getPassword());
        assertEquals(role, admin.getRole());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Admin admin = new Admin();
        String username = "adminUser";
        String password = "adminPass";
        String role = "SUPER_ADMIN";
        
        // Act
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setRole(role);
        
        // Assert
        assertEquals(username, admin.getUsername());
        assertEquals(password, admin.getPassword());
        assertEquals(role, admin.getRole());
    }

    @Test
    void testToString() {
        // Arrange
        String username = "adminUser";
        String password = "adminPass";
        String role = "ADMIN";
        Admin admin = new Admin(username, password, role);
        
        // Act
        String result = admin.toString();
        
        // Assert
        assertTrue(result.contains("username=" + username));
        assertTrue(result.contains("password=" + password));
        assertTrue(result.contains("role=" + role));
    }

}
