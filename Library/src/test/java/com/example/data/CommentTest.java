package com.example.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class CommentTest {

	@Test
     void testConstructor() {
        // Arrange
        Notice notice = new Notice(); // Assuming Notice has a default constructor
        Student student = new Student(); // Assuming Student has a default constructor
        Admin admin = new Admin(); // Assuming Admin has a default constructor
        String commentText = "This is a comment";
        LocalDateTime now = LocalDateTime.now();
        
        // Act
        Comment comment = new Comment(1L, notice, student, admin, commentText, now);

        // Assert
        assertEquals(1L, comment.getId());
        assertEquals(notice, comment.getNotice());
        assertEquals(student, comment.getStudent());
        assertEquals(admin, comment.getAdmin());
        assertEquals(commentText, comment.getCommentText());
        assertEquals(now, comment.getCreatedAt());
    }
	
	 @Test
	  void testGettersAndSetters() {
	        // Arrange
	        Comment comment = new Comment();
	        Notice notice = new Notice();
	        Student student = new Student();
	        Admin admin = new Admin();
	        String commentText = "Test comment";
	        LocalDateTime createdAt = LocalDateTime.now();
	        
	        // Act
	        comment.setId(1L);
	        comment.setNotice(notice);
	        comment.setStudent(student);
	        comment.setAdmin(admin);
	        comment.setCommentText(commentText);
	        comment.setCreatedAt(createdAt);
	        
	        // Assert
	        assertEquals(1L, comment.getId());
	        assertEquals(notice, comment.getNotice());
	        assertEquals(student, comment.getStudent());
	        assertEquals(admin, comment.getAdmin());
	        assertEquals(commentText, comment.getCommentText());
	        assertEquals(createdAt, comment.getCreatedAt());
	    }

	 @Test
	  void testToString() {
	        // Arrange
	        Notice notice = new Notice();
	        Student student = new Student();
	        Admin admin = new Admin();
	        String commentText = "Test comment";
	        LocalDateTime createdAt = LocalDateTime.now();
	        Comment comment = new Comment(1L, notice, student, admin, commentText, createdAt);
	        
	        // Act
	        String result = comment.toString();
	        
	        // Assert
	        assertTrue(result.contains("id=1"));
	        assertTrue(result.contains("notice=" + notice.toString()));
	        assertTrue(result.contains("student=" + student.toString()));
	        assertTrue(result.contains("admin=" + admin.toString()));
	        assertTrue(result.contains("commentText=" + commentText));
	        assertTrue(result.contains("createdAt=" + createdAt.toString()));
	    }
}
