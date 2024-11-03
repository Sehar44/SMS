package com.example.business;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
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

import com.example.accessor.NoticeAccessor;
import com.example.data.Notice;
import com.example.data.Student;
import com.example.exception.NoticeNotFoundException;

class NoticeServiceTest {

    @Mock
    private NoticeAccessor noticeAccessor;

    @InjectMocks
    private NoticeService noticeService;

    private Notice notice1;
    private Notice notice2;
    private Student student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create sample notices
        notice1 = new Notice();
        notice1.setId(1L);
        notice1.setSubject("Notice 1");
        notice1.setMessage("This is the first notice.");

        notice2 = new Notice();
        notice2.setId(2L);
        notice2.setSubject("Notice 2");
        notice2.setMessage("This is the second notice.");
        
        // Create a sample student
        student = new Student();
        student.setId(1L);
        student.setName("John Doe");
    }

    @Test
    void testGetAllNotices() {
        // Mock the behavior of the accessor
        when(noticeAccessor.getAllNotices()).thenReturn(Arrays.asList(notice1, notice2));

        // Call the service method
        List<Notice> notices = noticeService.getAllNotices();

        // Assertions
        assertNotNull(notices);
        assertEquals(2, notices.size());
        assertEquals("Notice 1", notices.get(0).getSubject());
        assertEquals("Notice 2", notices.get(1).getSubject());

        // Verify the accessor method is called
        verify(noticeAccessor, times(1)).getAllNotices();
    }

    @Test
    void testGetNoticeById() {
        // Mock the behavior of the accessor
        when(noticeAccessor.getNoticeById(1L)).thenReturn(notice1);

        // Call the service method
        Notice notice = noticeService.getNoticeById(1L);

        // Assertions
        assertNotNull(notice);
        assertEquals(1L, notice.getId());
        assertEquals("Notice 1", notice.getSubject());

        // Verify the accessor method is called
        verify(noticeAccessor, times(1)).getNoticeById(1L);
    }

    @Test
    void testAddNotice() {
        // Mock the behavior of the accessor
        when(noticeAccessor.saveNotice(any(Notice.class))).thenReturn(notice1);

        // Call the service method
        Notice savedNotice = noticeService.addNotice(notice1);

        // Assertions
        assertNotNull(savedNotice);
        assertEquals(1L, savedNotice.getId());
        assertEquals("Notice 1", savedNotice.getSubject());

        // Verify the accessor method is called
        verify(noticeAccessor, times(1)).saveNotice(notice1);
    } 

    @Test
    void testUpdateNotice() throws NoticeNotFoundException {
        // Mock the behavior of the accessor
        when(noticeAccessor.updateNotice(eq(1L), any(Notice.class))).thenReturn(notice1);

        // Call the service method
        Notice updatedNotice = noticeService.updateNotice(1L, notice1);

        // Assertions
        assertNotNull(updatedNotice);
        assertEquals(1L, updatedNotice.getId());
        assertEquals("Notice 1", updatedNotice.getSubject());

        // Verify the accessor method is called
        verify(noticeAccessor, times(1)).updateNotice(eq(1L), any(Notice.class));
    }

    @Test
    void testDeleteNotice() throws NoticeNotFoundException {
        // Call the service method
        noticeService.deleteNotice(1L);

        // Verify the accessor method is called
        verify(noticeAccessor, times(1)).deleteNotice(1L);
    }
    @Test
    void testHasStudentReacted() {
        Long studentId = student.getId();
        Long noticeId = notice1.getId();

        // Mock the behavior of the accessor
        when(noticeAccessor.hasStudentReacted(studentId, noticeId)).thenReturn(true);

        // Call the service method
        boolean hasReacted = noticeService.hasStudentReacted(studentId, noticeId);

        // Assertions
        assertTrue(hasReacted);

        // Verify the accessor method is called
        verify(noticeAccessor, times(1)).hasStudentReacted(studentId, noticeId);
    }

    @Test
    void testAddLike() {
        // Mocking the behavior of the accessor (this method doesn't return anything)
        doNothing().when(noticeAccessor).addLike(student, notice1);

        // Call the service method
        noticeService.addLike(student, notice1);

        // Verify the accessor method is called
        verify(noticeAccessor, times(1)).addLike(student, notice1);
    }

    @Test
    void testFindById() {
        Long noticeId = notice1.getId();

        // Mock the behavior of the accessor
        when(noticeAccessor.getNoticeById(noticeId)).thenReturn(notice1);

        // Call the service method
        Notice foundNotice = noticeService.findById(noticeId);

        // Assertions
        assertNotNull(foundNotice);
        assertEquals(noticeId, foundNotice.getId());
        assertEquals("Notice 1", foundNotice.getSubject());

        // Verify the accessor method is called
        verify(noticeAccessor, times(1)).getNoticeById(noticeId);
    }
}
