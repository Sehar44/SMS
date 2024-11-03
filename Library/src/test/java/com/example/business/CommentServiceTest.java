package com.example.business;

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

import com.example.accessor.CommentAccessor;
import com.example.data.Comment;

 class CommentServiceTest {

    @Mock
    private CommentAccessor commentAccessor;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
    }

    @Test
     void testGetCommentsByNoticeId() {
        // Given
        Long noticeId = 1L;
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        List<Comment> mockComments = Arrays.asList(comment1, comment2);
        when(commentAccessor.getCommentsByNoticeId(noticeId)).thenReturn(mockComments);

        // When
        List<Comment> result = commentService.getCommentsByNoticeId(noticeId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(commentAccessor, times(1)).getCommentsByNoticeId(noticeId);
    }

    @Test
     void testAddComment() {
        // Given
        Comment mockComment = new Comment();
        when(commentAccessor.addComment(mockComment)).thenReturn(mockComment);

        // When
        Comment result = commentService.addComment(mockComment);

        // Then
        assertNotNull(result);
        assertEquals(mockComment, result);
        verify(commentAccessor, times(1)).addComment(mockComment);
    }

    @Test
     void testDeleteComment() {
        // Given
        Long commentId = 1L;

        // When
        commentService.deleteComment(commentId);

        // Then
        verify(commentAccessor, times(1)).deleteComment(commentId);
    }

    @Test
     void testDeleteByNotice() {
        // Given
        Long noticeId = 1L;

        // When
        commentService.deleteByNotice(noticeId);

        // Then
        verify(commentAccessor, times(1)).deleteByNoticeId(noticeId);
    }
}
