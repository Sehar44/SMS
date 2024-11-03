package com.example.accessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.data.Likes;
import com.example.data.LikesRepository;
import com.example.data.Notice;
import com.example.data.NoticeRepository;
import com.example.data.Student;
import com.example.exception.NoticeNotFoundException;

 class NoticeAccessorTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private LikesRepository likesRepository;

    @InjectMocks
    private NoticeAccessor noticeAccessor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for getAllNotices
    @Test
     void testGetAllNotices() {
        List<Notice> notices = Arrays.asList(new Notice(), new Notice());
        when(noticeRepository.findAll()).thenReturn(notices);

        List<Notice> result = noticeAccessor.getAllNotices();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(noticeRepository, times(1)).findAll();
    }

    // Test for getNoticeById
    @Test
     void testGetNoticeById_NoticeExists() {
        Long noticeId = 1L;
        Notice notice = new Notice();
        notice.setId(noticeId);

        when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));

        Notice result = noticeAccessor.getNoticeById(noticeId);

        assertNotNull(result);
        assertEquals(noticeId, result.getId());
        verify(noticeRepository, times(1)).findById(noticeId);
    }

    @Test 
     void testGetNoticeById_NoticeNotFound() {
        Long noticeId = 1L;

        when(noticeRepository.findById(noticeId)).thenReturn(Optional.empty());

        Notice result = noticeAccessor.getNoticeById(noticeId);

        assertNull(result);
        verify(noticeRepository, times(1)).findById(noticeId);
    }

    // Test for saveNotice
    @Test
     void testSaveNotice() {
        Notice notice = new Notice();
        notice.setMessage("Test Notice");

        when(noticeRepository.save(notice)).thenReturn(notice);

        Notice result = noticeAccessor.saveNotice(notice);

        assertNotNull(result);
        assertEquals("Test Notice", result.getMessage());
        verify(noticeRepository, times(1)).save(notice);
    }

    // Test for updateNotice - existing notice
    @Test
     void testUpdateNotice_ExistingNotice() throws NoticeNotFoundException {
        Long noticeId = 1L;
        Notice existingNotice = new Notice();
        existingNotice.setId(noticeId);
        existingNotice.setMessage("Old Message");

        Notice updatedNotice = new Notice();
        updatedNotice.setMessage("New Message");

        when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(existingNotice));
        when(noticeRepository.save(existingNotice)).thenReturn(existingNotice);

        Notice result = noticeAccessor.updateNotice(noticeId, updatedNotice);

        assertNotNull(result);
        assertEquals("New Message", result.getMessage());
        verify(noticeRepository, times(1)).findById(noticeId);
        verify(noticeRepository, times(1)).save(existingNotice);
    }

    // Test for updateNotice - notice not found
    @Test
     void testUpdateNotice_NoticeNotFound() {
        Long noticeId = 1L;
        Notice updatedNotice = new Notice();

        when(noticeRepository.findById(noticeId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoticeNotFoundException.class, () -> {
            noticeAccessor.updateNotice(noticeId, updatedNotice);
        });

        assertEquals("Notice with ID " + noticeId + " not found", exception.getMessage());
        verify(noticeRepository, times(1)).findById(noticeId);
        verify(noticeRepository, times(0)).save(any());
    }

    // Test for deleteNotice - existing notice
    @Test
     void testDeleteNotice_ExistingNotice() throws NoticeNotFoundException {
        Long noticeId = 1L;

        when(noticeRepository.existsById(noticeId)).thenReturn(true);

        noticeAccessor.deleteNotice(noticeId);

        verify(noticeRepository, times(1)).deleteById(noticeId);
    }

    // Test for deleteNotice - notice not found
    @Test
     void testDeleteNotice_NoticeNotFound() {
        Long noticeId = 1L;

        when(noticeRepository.existsById(noticeId)).thenReturn(false);

        Exception exception = assertThrows(NoticeNotFoundException.class, () -> {
            noticeAccessor.deleteNotice(noticeId);
        });

        assertEquals("Notice with ID " + noticeId + " not found", exception.getMessage());
        verify(noticeRepository, times(0)).deleteById(any());
    }

    // Test for hasStudentReacted
    @Test
     void testHasStudentReacted_True() {
        Long studentId = 1L;
        Long noticeId = 1L;
        Likes like = new Likes();

        when(likesRepository.findByStudentIdAndNoticeId(studentId, noticeId)).thenReturn(like);

        boolean result = noticeAccessor.hasStudentReacted(studentId, noticeId);

        assertTrue(result);
        verify(likesRepository, times(1)).findByStudentIdAndNoticeId(studentId, noticeId);
    }

    @Test
     void testHasStudentReacted_False() {
        Long studentId = 1L;
        Long noticeId = 1L;

        when(likesRepository.findByStudentIdAndNoticeId(studentId, noticeId)).thenReturn(null);

        boolean result = noticeAccessor.hasStudentReacted(studentId, noticeId);

        assertFalse(result);
        verify(likesRepository, times(1)).findByStudentIdAndNoticeId(studentId, noticeId);
    }

    // Test for addLike
    @Test
     void testAddLike() {
        Student student = new Student();
        student.setId(1L);

        Notice notice = new Notice();
        notice.setId(1L);

        noticeAccessor.addLike(student, notice);

        verify(likesRepository, times(1)).save(any(Likes.class));
    }

    // Test for findById
    @Test
     void testFindById_NoticeExists() {
        Long noticeId = 1L;
        Notice notice = new Notice();
        notice.setId(noticeId);

        when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));

        Notice result = noticeAccessor.findById(noticeId);

        assertNotNull(result);
        assertEquals(noticeId, result.getId());
        verify(noticeRepository, times(1)).findById(noticeId);
    }

    @Test
     void testFindById_NoticeNotFound() {
        Long noticeId = 1L;

        when(noticeRepository.findById(noticeId)).thenReturn(Optional.empty());

        Notice result = noticeAccessor.findById(noticeId);

        assertNull(result);
        verify(noticeRepository, times(1)).findById(noticeId);
    }
}
