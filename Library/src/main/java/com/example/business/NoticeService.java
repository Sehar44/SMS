package com.example.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.accessor.NoticeAccessor;
import com.example.data.Notice;
import com.example.data.Student;
import com.example.exception.NoticeNotFoundException;

@Service
public class NoticeService {

	
	private final NoticeAccessor noticeAccessor;
	
    @Autowired
	public NoticeService(NoticeAccessor noticeAccessor) {
		this.noticeAccessor = noticeAccessor;
	}


	public List<Notice> getAllNotices() {
		return noticeAccessor.getAllNotices();
	}

	public Notice getNoticeById(Long id) {
		return noticeAccessor.getNoticeById(id);
	}

	public Notice addNotice(Notice notice) {
		return noticeAccessor.saveNotice(notice);
	}

	// Update an existing notice
	public Notice updateNotice(Long id, Notice updatedNotice) throws NoticeNotFoundException {
		return noticeAccessor.updateNotice(id, updatedNotice);
	}

	// Delete a notice by ID
	public void deleteNotice(Long id) throws NoticeNotFoundException {
		noticeAccessor.deleteNotice(id);
	}
	
	public boolean hasStudentReacted(Long studentId, Long noticeId) {
		return noticeAccessor.hasStudentReacted(studentId, noticeId);
	}
	
	public void addLike(Student student, Notice notice) {
		noticeAccessor.addLike(student, notice);
	}
	
	public Notice findById(Long id) {
		return noticeAccessor.getNoticeById(id);
	}
}
