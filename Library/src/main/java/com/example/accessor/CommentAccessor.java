package com.example.accessor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.data.Comment;
import com.example.data.CommentRepository;

@Component
public class CommentAccessor {
	
	
	
	
	private final CommentRepository commentRepository;

	@Autowired
	public CommentAccessor(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}
	
	public List<Comment> getCommentsByNoticeId(Long noticeId) {
		return commentRepository.findByNoticeId(noticeId);	
		
	}
	
	public Comment addComment(Comment comment) {
		return commentRepository.save(comment);
	}
	
	public void deleteComment(Long commentId) {
		commentRepository.deleteById(commentId);
	}
	
	public void deleteByNoticeId(Long noticeId) {
		commentRepository.deleteByNoticeId(noticeId);
	}
}
