package com.example.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.accessor.StudentAccessor;
import com.example.data.Student;
import com.example.exception.InvalidAgeException;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.UserNotFoundException;

@Service
public class StudentService {
	
	
	
	private final StudentAccessor studentAccessor;

	@Autowired
	public StudentService(StudentAccessor studentAccessor) {
		this.studentAccessor = studentAccessor;
	}

	public Page<Student> fetchFilteredStudents(int page, int rows, String sidx, String sord, String name, String email,
			String course) {
		return studentAccessor.fetchFilteredStudents(page, rows, sidx, sord, name, email, course);
	}

	public Page<Student> fetchPaginatedStudents(int page, int size) {
		return studentAccessor.getPaginatedStudents(page, size);
	}

	public List<Student> fetchAllStudents() {
		return studentAccessor.getAllStudents();
	}
	
	public List<Student> fetchAllActiveStudents() {
		return studentAccessor.getAllActiveStudents();
	}

	public Student fetchStudentById(Long id) {
		return studentAccessor.getStudentById(id);
	}

	public Student addStudent(Student student) throws UserAlreadyExistsException, InvalidAgeException {

		return studentAccessor.saveStudent(student);
	}

// Update an existing student
	public Student updateStudent(Long id, Student updatedStudent) throws UserAlreadyExistsException, InvalidAgeException, UserNotFoundException {
		return studentAccessor.updateStudent(id, updatedStudent);
	}

	// Delete a student by ID
	public void deleteStudent(Long id) throws UserNotFoundException {
		studentAccessor.deleteStudent(id);
	}

	public Student findByEmail(String username) {
		
		return studentAccessor.findByEmail(username);
	}
	
	public void softDeleteStudent(Long id, String deletedBy) throws UserNotFoundException {
		studentAccessor.softDeleteStudent(id, deletedBy);
	}

	public void restoreStudent(Long id,String restoredBy) throws UserNotFoundException {
		studentAccessor.restoreStudent(id,restoredBy);
	}
	
	public List<Student> getDeletedStudents() {
		return studentAccessor.getDeletedStudent();
	}

}
