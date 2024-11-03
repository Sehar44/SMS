package com.example.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.data.Admin;
import com.example.data.AdminRepository;

 class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
    }

    @Test
     void testGetAdminByUsername() {
        // Given
        String username = "adminUser";
        Admin mockAdmin = new Admin();
        mockAdmin.setUsername(username);
        when(adminRepository.findByUsername(username)).thenReturn(mockAdmin);

        // When
        Admin result = adminService.getAdminByUsername(username);

        // Then
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(adminRepository, times(1)).findByUsername(username);
    }

    @Test
     void testGetAdminByUsername_AdminNotFound() {
        // Given
        String username = "nonExistentUser";
        when(adminRepository.findByUsername(username)).thenReturn(null);

        // When
        Admin result = adminService.getAdminByUsername(username);

        // Then
        assertNull(result);
        verify(adminRepository, times(1)).findByUsername(username);
    }
}
