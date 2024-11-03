package com.example.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NoticeTest {

	private Notice notice;
    private Comment comment1;
    private Comment comment2;

    @BeforeEach
     void setUp() {
        // Create a list of sample comments for testing
        comment1 = new Comment();
        comment2 = new Comment();
        List<Comment> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);

        // Initialize the Notice object
        notice = new Notice(1L, "Sample Subject", "Sample Message");
        notice.setComments(comments);
    }

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        notice = new Notice();

        // Assert
        assertNull(notice.getId());
        assertNull(notice.getSubject());
        assertNull(notice.getMessage());
        assertNull(notice.getComments());
        assertEquals(0, notice.getThumbsUp());
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange & Act
        notice = new Notice(1L, "Subject", "Message");

        // Assert
        assertEquals(1L, notice.getId());
        assertEquals("Subject", notice.getSubject());
        assertEquals("Message", notice.getMessage());
        assertEquals(0, notice.getThumbsUp()); // Ensure thumbsUp is initialized to 0
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Long id = 2L;
        String subject = "New Subject";
        String message = "New Message";
        Integer thumbsUp = 5;

        // Act
        notice.setId(id);
        notice.setSubject(subject);
        notice.setMessage(message);
        notice.setThumbsUp(thumbsUp);

        // Assert
        assertEquals(id, notice.getId());
        assertEquals(subject, notice.getSubject());
        assertEquals(message, notice.getMessage());
        assertEquals(thumbsUp, notice.getThumbsUp());
    }

   

    @Test
    void testComments() {
        // Arrange
        List<Comment> comments = new ArrayList<>();
        Comment newComment = new Comment();
        comments.add(newComment);

        // Act
        notice.setComments(comments);

        // Assert
        assertEquals(1, notice.getComments().size());
        assertEquals(newComment, notice.getComments().get(0));
    }

    @Test
     void testToString() {
        // Arrange
        notice = new Notice(1L, "Subject", "Message");

        // Act
        String result = notice.toString();

        // Assert
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("subject=Subject"));
        assertTrue(result.contains("message=Message"));
    }

}
