package com.example.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentAuditTest {

	private StudentAudit studentAudit;

    @BeforeEach
    void setUp() {
        // Initializing a new instance before each test
        studentAudit = new StudentAudit();
    }

    @Test
    void testConstructorWithArguments() {
        LocalDateTime updatedAt = LocalDateTime.now();
        StudentAudit audit = new StudentAudit(1L, 101L, "Admin", "User", updatedAt);

        assertEquals(1L, audit.getId());
        assertEquals(101L, audit.getStudentId());
        assertEquals("Admin", audit.getDeletedBy());
        assertEquals("User", audit.getRestoredBy());
        assertEquals(updatedAt, audit.getUpdatedAt());
    }

    @Test
    void testNoArgsConstructor() {
        // Ensure the default constructor creates an object with null values
        assertNotNull(studentAudit);
        assertNull(studentAudit.getId());
        assertNull(studentAudit.getStudentId());
        assertNull(studentAudit.getDeletedBy());
        assertNull(studentAudit.getRestoredBy());
        assertNull(studentAudit.getUpdatedAt());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        LocalDateTime updatedAt = LocalDateTime.now();

        // Act
        studentAudit.setId(1L);
        studentAudit.setStudentId(101L);
        studentAudit.setDeletedBy("Admin");
        studentAudit.setRestoredBy("User");
        studentAudit.setUpdatedAt(updatedAt);

        // Assert
        assertEquals(1L, studentAudit.getId());
        assertEquals(101L, studentAudit.getStudentId());
        assertEquals("Admin", studentAudit.getDeletedBy());
        assertEquals("User", studentAudit.getRestoredBy());
        assertEquals(updatedAt, studentAudit.getUpdatedAt());
    }

    @Test
    void testToString() {
        // Arrange
        LocalDateTime updatedAt = LocalDateTime.now();
        studentAudit = new StudentAudit(1L, 101L, "Admin", "User", updatedAt);

        String expectedToString = "StudentAudit [id=1, studentId=101, deletedBy=Admin, restoredBy=User, deletedAt=" + updatedAt + "]";
        
        // Act and Assert
        assertEquals(expectedToString, studentAudit.toString());
    }

}
