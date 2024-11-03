package com.example.data;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Notice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String subject;
	private String message;
	@CreationTimestamp // Automatically sets the timestamp when the entity is created
	private LocalDateTime createdAt;


	@Column(nullable = true)
	private Integer thumbsUp = 0; // Thumbs up reaction


	 @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
	    private List<Comment> comments;  // List of comments for this notice

	    // Getters and setters for comments
	    public List<Comment> getComments() {
	        return comments;
	    }

	    public void setComments(List<Comment> comments) {
	        this.comments = comments;
	    }
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	

	public Integer getThumbsUp() {
		return thumbsUp;
	}

	public void setThumbsUp(Integer thumbsUp) {
		this.thumbsUp = thumbsUp;
	}

	
	@Override
	public String toString() {
		return "Notice [id=" + id + ", subject=" + subject + ", message=" + message + "]";
	}

	public Notice(Long id, String subject, String message) {
		super();
		this.id = id;
		this.subject = subject;
		this.message = message;
	}

	public Notice() {
		super();

	}

}