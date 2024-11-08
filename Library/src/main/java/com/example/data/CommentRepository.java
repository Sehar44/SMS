package com.example.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	List<Comment> findByNoticeId(Long noticeId);

	void deleteByNoticeId(Long noticeId);
}
