package com.example.data;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class StudentAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long studentId;
	private String deletedBy;
	private String restoredBy;
	private LocalDateTime updatedAt;
	
	
	
	public String getRestoredBy() {
		return restoredBy;
	}
	public void setRestoredBy(String restoredBy) {
		this.restoredBy = restoredBy;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public String getDeletedBy() {
		return deletedBy;
	}
	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	@Override
	public String toString() {
		return "StudentAudit [id=" + id + ", studentId=" + studentId + ", deletedBy=" + deletedBy + ", restoredBy="
				+ restoredBy + ", deletedAt=" + updatedAt + "]";
	}
	
	public StudentAudit(Long id, Long studentId, String deletedBy, String restoredBy, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.studentId = studentId;
		this.deletedBy = deletedBy;
		this.restoredBy = restoredBy;
		this.updatedAt = updatedAt;
	}
	public StudentAudit() {
		super();
		
	}
	
	
	
	
}
