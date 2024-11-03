package com.example.accessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.data.Comment;
import com.example.data.CommentRepository;

 class CommentAccessorTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentAccessor commentAccessor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
    }

    @Test
     void testGetCommentsByNoticeId() {
        // Given
        Long noticeId = 1L;
        List<Comment> mockComments = Arrays.asList(new Comment(), new Comment());
        when(commentRepository.findByNoticeId(noticeId)).thenReturn(mockComments);

        // When
        List<Comment> result = commentAccessor.getCommentsByNoticeId(noticeId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(commentRepository, times(1)).findByNoticeId(noticeId);
    }

    @Test
     void testAddComment() {
        // Given
        Comment comment = new Comment();
        when(commentRepository.save(comment)).thenReturn(comment);

        // When
        Comment result = commentAccessor.addComment(comment);

        // Then
        assertNotNull(result);
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
     void testDeleteComment() {
        // Given
        Long commentId = 1L;

        // When
        commentAccessor.deleteComment(commentId);

        // Then
        verify(commentRepository, times(1)).deleteById(commentId);
    }

    @Test
     void testDeleteByNoticeId() {
        // Given
        Long noticeId = 1L;

        // When
        commentAccessor.deleteByNoticeId(noticeId);

        // Then
        verify(commentRepository, times(1)).deleteByNoticeId(noticeId);
    }
}
