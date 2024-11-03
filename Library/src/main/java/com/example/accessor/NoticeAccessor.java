package com.example.accessor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.data.Likes;
import com.example.data.LikesRepository;
import com.example.data.Notice;
import com.example.data.NoticeRepository;
import com.example.data.Student;
import com.example.exception.NoticeNotFoundException;

@Component
public class NoticeAccessor {
	
	private static final Logger logger = LoggerFactory.getLogger(NoticeAccessor.class);


	
	private final NoticeRepository noticeRepository;
	
	
	private final LikesRepository likesRepository;
	

	@Autowired
	public NoticeAccessor(NoticeRepository noticeRepository, LikesRepository likesRepository) {
		this.noticeRepository = noticeRepository;
		this.likesRepository = likesRepository;
	}

	public List<Notice> getAllNotices() {
		 logger.info("Fetching all notices");
	        List<Notice> notices = noticeRepository.findAll();
	        logger.debug("Notices fetched: {}", notices.size());
	        return notices;
	}

	public Notice getNoticeById(Long id) {
		// Implementation to find notice by ID in the database
		return noticeRepository.findById(id).orElse(null);
	}

	public Notice saveNotice(Notice notice) {
		return noticeRepository.save(notice);
	}

	public Notice updateNotice(Long id, Notice updatedNotice) throws NoticeNotFoundException {
		Notice existingNotice = noticeRepository.findById(id).orElse(null);
		if (existingNotice != null) {
			existingNotice.setMessage(updatedNotice.getMessage());
			existingNotice.setSubject(updatedNotice.getSubject());
			return noticeRepository.save(existingNotice);
		} else {
			logger.error("Notice with ID {} not found", id);
			throw new NoticeNotFoundException("Notice with ID " + id + " not found");
		}
	}

	// Delete a notice by ID
	public void deleteNotice(Long id) throws NoticeNotFoundException {
		if (noticeRepository.existsById(id)) {
			noticeRepository.deleteById(id);
		} else {
			logger.error("Notice with ID {} not found", id);
			throw new NoticeNotFoundException("Notice with ID " + id + " not found");
		}
	}
	
	public boolean hasStudentReacted(Long studentId, Long noticeId) {
		return likesRepository.findByStudentIdAndNoticeId(studentId, noticeId)!=null;
	}
	
	public void addLike(Student student, Notice notice) {
		Likes likes=new Likes();
		likes.setStudent(student);
		likes.setNotice(notice);
		logger.debug("Likes added: {}", likes);
		likesRepository.save(likes);
	}
	
	public Notice findById(Long noticeId) {
		return noticeRepository.findById(noticeId).orElse(null);
	}
}
