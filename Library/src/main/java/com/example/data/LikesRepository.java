package com.example.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long>{
	Likes findByStudentIdAndNoticeId(Long studentId, Long noticeId);
	void deleteByNotice(Notice notice);
}
