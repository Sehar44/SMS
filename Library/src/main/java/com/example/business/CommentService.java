package com.example.business;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.accessor.CommentAccessor;
import com.example.data.Comment;

@Service
public class CommentService {
	
	
	private final CommentAccessor commentAccessor;
	
	public CommentService(CommentAccessor commentAccessor) {
		this.commentAccessor = commentAccessor;
	}
	
	public List<Comment> getCommentsByNoticeId(Long noticeId) {
		return commentAccessor.getCommentsByNoticeId(noticeId);	
		
	}
	
	public Comment addComment(Comment comment) {
		return commentAccessor.addComment(comment);
	}
	
	public void deleteComment(Long commentId) {
		commentAccessor.deleteComment(commentId);
	}
	
	public void deleteByNotice(Long noticeId) {
		commentAccessor.deleteByNoticeId(noticeId);
	}
}
