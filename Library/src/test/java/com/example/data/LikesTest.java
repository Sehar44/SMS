package com.example.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LikesTest {

	private Likes likes;
    private Student student;
    private Notice notice;

    @BeforeEach
    void setUp() {
        // Set up student and notice objects for testing
        student = new Student();
        notice = new Notice();
    }

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        likes = new Likes();

        // Assert
        assertNull(likes.getId());
        assertNull(likes.getStudent());
        assertNull(likes.getNotice());
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        Long id = 1L;

        // Act
        likes = new Likes(id, student, notice);

        // Assert
        assertEquals(id, likes.getId());
        assertEquals(student, likes.getStudent());
        assertEquals(notice, likes.getNotice());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Long id = 1L;

        // Act
        likes = new Likes();
        likes.setId(id);
        likes.setStudent(student);
        likes.setNotice(notice);

        // Assert
        assertEquals(id, likes.getId());
        assertEquals(student, likes.getStudent());
        assertEquals(notice, likes.getNotice());
    }

    @Test
    void testToString() {
        // Arrange
        Long id = 1L;
        likes = new Likes(id, student, notice);

        // Act
        String result = likes.toString();

        // Assert
        assertTrue(result.contains("id=" + id));
        assertTrue(result.contains("student=" + student));
        assertTrue(result.contains("notice=" + notice));
    }

}
