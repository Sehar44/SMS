package com.example.accessor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.example.data.Student;
import com.example.data.StudentAudit;
import com.example.data.StudentAuditRepository;
import com.example.data.StudentRepository;
import com.example.exception.InvalidAgeException;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.UserNotFoundException;

@Component
public class StudentAccessor {
	
	private static final Logger logger = LoggerFactory.getLogger(StudentAccessor.class);

	
	private final StudentRepository studentRepository;

	 private static final String STUDENT_WITH_ID = "Student with ID: {}";
	 private static final String NOT_FOUND="not found";
	 private static final String STUDENT_NOT_FOUND = STUDENT_WITH_ID + NOT_FOUND;
	 

	
	private final StudentAuditRepository studentAuditRepository;

	@Autowired
	public StudentAccessor(StudentRepository studentRepository, StudentAuditRepository studentAuditRepository) {
		this.studentRepository = studentRepository;
		this.studentAuditRepository = studentAuditRepository;

	}
	


	
	public Page<Student> fetchFilteredStudents(int page, int size, String sortField, String sortOrder, String name,
			String email, String course) {
		Pageable pageable = PageRequest.of(page, size,
				Sort.by(sortOrder.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));

		// Logging parameters for debugging
		 logger.info("Fetching students with parameters - Name: {}, Email: {}, Course: {}, Page: {}, Size: {}", name, email, course, page, size);

		// Fetch students by filters (name, email, course)
		if(name==null && email==null && course==null) {
			Page<Student> result= studentRepository.findByIsActive(0, pageable);
			//System.out.println(result); // Log the result
			logger.info("Fetched active students: {}", result.getContent());
			return result;
		}
	
		Page<Student> result = studentRepository.findByFilters(name, email, course, pageable);
		
		//System.out.println(result); // Log the result
		 logger.info("Fetched filtered students: {}", result.getContent());
		return result;
	}

	public List<Student> getAllStudents() {
		 logger.info("Fetching all students");
	        List<Student> students = studentRepository.findAll();
	        logger.info("Fetched {} students", students.size());
	        return students;
	}
	
	public List<Student> getAllActiveStudents() {
        logger.info("Fetching all active students");
        List<Student> activeStudents = studentRepository.findByIsActive(0);
        logger.info("Fetched {} active students", activeStudents.size());
        return activeStudents;

	}

	public Page<Student> getPaginatedStudents(int page, int size) {
		 logger.info("Fetching paginated students, Page: {}, Size: {}", page, size);
	        return studentRepository.findAll(PageRequest.of(page, size));
	}

	public Student getStudentById(Long id) {
		 logger.info("Fetching student by ID: {}", id);
		// Implementation to find student by ID in the database
		return studentRepository.findById(id).orElse(null);
	}

	public Student saveStudent(Student student) throws UserAlreadyExistsException, InvalidAgeException {
		
		logger.info("Saving student with email: {}", student.getEmail());

		
		if (studentRepository.findByEmail(student.getEmail()) != null) {
			throw new UserAlreadyExistsException("Student with given email already exists");
		}
		if(student.getAge()<16) {
			throw new InvalidAgeException("Invalid age!!");
		}
		student.setIsActive(0);
		return studentRepository.save(student);
	}

	public Student updateStudent(Long id, Student updatedStudent) throws UserAlreadyExistsException, InvalidAgeException, UserNotFoundException {
		logger.info("Updating student with ID: {}", id);
		Optional<Student> studentOptional = studentRepository.findById(id);

		if (studentOptional.isPresent()) {
			
			Student studentWithSameEmail = studentRepository.findByEmail(updatedStudent.getEmail());
	        if (studentWithSameEmail != null && !studentWithSameEmail.getId().equals(id)) {
	        	logger.error("Student with email: {} already exists", updatedStudent.getEmail());
	            throw new UserAlreadyExistsException("Student with the given email already exists");
	        }
	        if(updatedStudent.getAge()<16) {
	        	  logger.error("Invalid age for student: {}", updatedStudent.getAge());
				throw new InvalidAgeException("Invalid age!!");
			}
			Student existingStudent = studentOptional.get();
			existingStudent.setName(updatedStudent.getName());
			existingStudent.setEmail(updatedStudent.getEmail());
			existingStudent.setAge(updatedStudent.getAge());
			existingStudent.setCourse(updatedStudent.getCourse());
			// Update other fields as necessary
			return studentRepository.save(existingStudent);
		} else {
			logger.error(STUDENT_NOT_FOUND, id);
			throw new UserNotFoundException(STUDENT_WITH_ID + id + NOT_FOUND);
		}
	}

	// Delete a student by ID
	public void deleteStudent(Long id) throws UserNotFoundException {
		 logger.info("Deleting student with ID: {}", id);
		if (studentRepository.existsById(id)) {
			studentRepository.deleteById(id);
			logger.info("Successfully deleted student with ID: {}", id);
		} else {
			logger.error(STUDENT_NOT_FOUND, id);
			throw new UserNotFoundException(STUDENT_WITH_ID + id +  NOT_FOUND);
		}
	}

	public Student findByEmail(String email) {

		logger.info("Fetching student by email: {}", email);
		return studentRepository.findByEmail(email);
	}

	public void softDeleteStudent(Long id, String deletedBy) throws UserNotFoundException {
	    logger.info("Soft deleting student with ID: {} by {}", id, deletedBy);
	    
	    Optional<Student> optionalStudent = studentRepository.findById(id);
	    
	    if (optionalStudent.isPresent()) {
	        Student student = optionalStudent.get();
	        student.setIsActive(1);
	        studentRepository.save(student);

	        // Create a new StudentAudit entry
	        StudentAudit audit = new StudentAudit();
	        audit.setStudentId(id);
	        audit.setDeletedBy(deletedBy);
	        audit.setUpdatedAt(LocalDateTime.now());
	        studentAuditRepository.save(audit);

	        logger.info("Successfully soft deleted student with ID: {}", id);
	    } else {
	        logger.error(STUDENT_NOT_FOUND, id);
	        throw new UserNotFoundException(STUDENT_WITH_ID + id +  NOT_FOUND);
	    }
	}

	

	
	public void restoreStudent(Long id,String restoredBy) throws UserNotFoundException {
	    Optional<Student> studentOptional = studentRepository.findById(id);
	    if (studentOptional.isPresent()) {
	        Student student = studentOptional.get();
	        student.setIsActive(0);
	        studentRepository.save(student);
	        StudentAudit audit= new StudentAudit();
	        audit.setStudentId(id);
	        audit.setRestoredBy(restoredBy);
	        audit.setUpdatedAt(LocalDateTime.now());
	        studentAuditRepository.save(audit);
	        
	        logger.info("Successfully restored student with ID: {}", id);
	    } else {
	        logger.error(STUDENT_NOT_FOUND, id);
	        throw new UserNotFoundException(STUDENT_WITH_ID + id +  NOT_FOUND);
	    }
	}

	
	public List<Student> getDeletedStudent(){
		return studentRepository.findByIsActive(1);
	
		
	}
}
