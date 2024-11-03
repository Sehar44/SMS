package com.example.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	private Integer age;
	private String course;
	private String password;
	private Integer isActive;
	
	// New field to store the image file path
    private String profileImagePath;

	public String getProfileImagePath() {
		return profileImagePath;
	}
	
	public void setProfileImagePath(String profileImagePath) {
		this.profileImagePath=profileImagePath;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Integer getIsActive() {
		return isActive;
	}
	
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}


	
	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", email=" + email + ", age=" + age + ", course=" + course
				+ ", password=" + password + ", isActive=" + isActive + ", profileImagePath=" + profileImagePath + "]";
	}

	
	public Student(Long id, String name, String email, Integer age, String course) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.age = age;
		this.course = course;
		this.isActive = 0;
	}
	

	public Student() {
		super();
		
	}

	

}
