package com.example.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Notice notice;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}

	public Likes(Long id, Student student, Notice notice) {
		super();
		this.id = id;
		this.student = student;
		this.notice = notice;
	}

	public Likes() {
		super();

	}

	@Override
	public String toString() {
		return "ThumbsUp [id=" + id + ", student=" + student + ", notice=" + notice + "]";
	}
	
	
}