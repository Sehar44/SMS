
package com.example.accessor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.example.data.Student;
import com.example.data.StudentAudit;
import com.example.data.StudentAuditRepository;
import com.example.data.StudentRepository;
import com.example.exception.InvalidAgeException;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.UserNotFoundException;

class StudentAccessorTest {
	
	 @Mock
	    private StudentRepository studentRepository;

	    @Mock
	    private StudentAuditRepository studentAuditRepository;

    @InjectMocks
    private StudentAccessor studentAccessor;


    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample student objects for testing
        student1 = new Student();
        student1.setId(1L);
        student1.setName("John Doe");
        student1.setEmail("john@example.com");
        student1.setAge(20);
        student1.setIsActive(0);
        student1.setCourse("CSE");

        student2 = new Student();
        student2.setId(2L);
        student2.setName("Jane Doe");
        student2.setEmail("jane@example.com");
        student2.setAge(22);
        student2.setIsActive(0);
        student2.setCourse("ECE");
    }

    @Test
    void testGetAllStudents() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));
        List<Student> students = studentAccessor.getAllStudents();
        assertEquals(2, students.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentById() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
        Student student = studentAccessor.getStudentById(1L);
        assertNotNull(student);
        assertEquals("John Doe", student.getName());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveStudent() throws UserAlreadyExistsException, InvalidAgeException {
        when(studentRepository.save(any(Student.class))).thenReturn(student1);
        when(studentRepository.findByEmail(anyString())).thenReturn(null);

        Student savedStudent = studentAccessor.saveStudent(student1);
        assertNotNull(savedStudent);
        assertEquals("john@example.com", savedStudent.getEmail());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testSaveStudentAlreadyExists() {
        when(studentRepository.findByEmail("john@example.com")).thenReturn(student1);

        assertThrows(UserAlreadyExistsException.class, () -> {
            studentAccessor.saveStudent(student1);
        });
    }

    @Test
    void testSaveStudentInvalidAge() {
        student1.setAge(15); // Invalid age

        assertThrows(InvalidAgeException.class, () -> {
            studentAccessor.saveStudent(student1);
        });
    }

    @Test
    void testUpdateStudent() throws UserAlreadyExistsException, InvalidAgeException, UserNotFoundException {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
        when(studentRepository.findByEmail("jane@example.com")).thenReturn(null);
        when(studentRepository.save(any(Student.class))).thenReturn(student2);

        Student updatedStudent = studentAccessor.updateStudent(1L, student2);
        assertNotNull(updatedStudent);
        assertEquals("Jane Doe", updatedStudent.getName());
        verify(studentRepository, times(1)).save(any(Student.class));
    }
    
    @Test
    void testUpdateStudentThrowsUserAlreadyExistsException() {
        // Given
        Student existingStudentWithEmail = new Student(2L, "Jane Doe", "jane@example.com", 20, "CSE");
        
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1)); // student1 has ID 1
        when(studentRepository.findByEmail("jane@example.com")).thenReturn(existingStudentWithEmail); // Another student exists with this email

        // When & Then
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            studentAccessor.updateStudent(1L, student2); // student2 has the same email as existingStudentWithEmail
        });

        assertEquals("Student with the given email already exists", exception.getMessage());
        
        verify(studentRepository, never()).save(any(Student.class)); // Ensure save is never called
    }

    
    @Test
    void testUpdateStudentThrowsInvalidAgeException() {
        // Given
        student2.setAge(15); // Set invalid age

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1)); // student1 has ID 1
        when(studentRepository.findByEmail("jane@example.com")).thenReturn(null); // No email conflict

        // When & Then
        InvalidAgeException exception = assertThrows(InvalidAgeException.class, () -> {
            studentAccessor.updateStudent(1L, student2); // student2 has age < 16
        });

        assertEquals("Invalid age!!", exception.getMessage());
        
        verify(studentRepository, never()).save(any(Student.class)); // Ensure save is never called
    }

    @Test
    void testUpdateStudentThrowsStudentNotFoundException() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.empty()); // No student found with ID 1

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            studentAccessor.updateStudent(1L, student2); // Attempt to update non-existent student
        });

        assertEquals("Student with ID: {}1not found", exception.getMessage());
        
        verify(studentRepository, never()).save(any(Student.class)); // Ensure save is never called
    }


    @Test
    void testDeleteStudent() throws UserNotFoundException {
        when(studentRepository.existsById(1L)).thenReturn(true);

        studentAccessor.deleteStudent(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteStudent_UserNotFoundException() {
        // Given that student with id 1L does not exist
        when(studentRepository.existsById(1L)).thenReturn(false);

        // Assert that the exception is thrown when deleting a non-existing student
        assertThrows(UserNotFoundException.class, () -> {
            studentAccessor.deleteStudent(1L);
        });

        // Verify that deleteById is never called since the student does not exist
        verify(studentRepository, never()).deleteById(1L);
    }

    @Test
  void testFetchFilteredStudents() {
      // Given
      int page = 0;
      int size = 10;
      String sortField = "name";
      String sortOrder = "asc";
      String name = "John";
      String email = "john@example.com";
      String course = "CSE";

      // Create a list of students to be returned by the mock repository
      List<Student> students = Arrays.asList(
              new Student(1L, "John Doe", "john@example.com",19, "CSE"),
              new Student(2L, "Jane Doe", "jane@example.com",20, "ECE")
      ); 
      Page<Student> studentPage = new PageImpl<>(students, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortField)), students.size());

      // Mock the repository call
      when(studentRepository.findByFilters(name, email, course, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortField))))
              .thenReturn(studentPage);

      // When
      Page<Student> result = studentAccessor.fetchFilteredStudents(page, size, sortField, sortOrder, name, email, course);

      // Then
      assertNotNull(result);
      assertEquals(2, result.getTotalElements());
      assertEquals("John Doe", result.getContent().get(0).getName());
      assertEquals("jane@example.com", result.getContent().get(1).getEmail());

      // Verify the repository was called with the correct parameters
      verify(studentRepository).findByFilters(name, email, course, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortField)));
  }

  @Test
  void testFetchFilteredStudentsWithDescendingSortOrder() {
      // Given
      int page = 0;
      int size = 10;
      String sortField = "name";
      String sortOrder = "desc";
      String name = "John";
      String email = "john@example.com";
      String course = "CSE";

      // Create a list of students to be returned by the mock repository
      List<Student> students = Arrays.asList(
              new Student(1L, "John Doe", "john@example.com",20, "CSE"),
              new Student(2L, "Jane Doe", "jane@example.com",21, "ECE")
      );
      Page<Student> studentPage = new PageImpl<>(students, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortField)), students.size());

      // Mock the repository call
      when(studentRepository.findByFilters(name, email, course, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortField))))
              .thenReturn(studentPage);

      // When
      Page<Student> result = studentAccessor.fetchFilteredStudents(page, size, sortField, sortOrder, name, email, course);

      // Then
      assertNotNull(result);
      assertEquals(2, result.getTotalElements());
      assertEquals("John Doe", result.getContent().get(0).getName());

      // Verify the repository was called with the correct parameters
      verify(studentRepository).findByFilters(name, email, course, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortField)));
  }

  
    @Test
    void testGetDeletedStudents() {
        when(studentRepository.findByIsActive(1)).thenReturn(Arrays.asList(student1, student2));
        List<Student> deletedStudents = studentAccessor.getDeletedStudent();
        assertEquals(2, deletedStudents.size());
        verify(studentRepository, times(1)).findByIsActive(1);
    }
    
    @Test
    void testFetchActiveStudentsWhenFiltersAreNull() {
        // Given
        int page = 0;
        int size = 10;
        String sortField = "name";
        String sortOrder = "asc";
        String name = null;
        String email = null;
        String course = null;

        // Create a list of students to be returned by the mock repository
        List<Student> activeStudents = Arrays.asList(
                new Student(1L, "John Doe", "john@example.com",19, "CSE"),
                new Student(2L, "Jane Doe", "jane@example.com",20, "ECE")
        ); 
        Page<Student> studentPage = new PageImpl<>(activeStudents, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortField)), activeStudents.size());

        // Mock the repository call for fetching active students
        when(studentRepository.findByIsActive(0, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortField))))
                .thenReturn(studentPage);

        // When
        Page<Student> result = studentAccessor.fetchFilteredStudents(page, size, sortField, sortOrder, name, email, course);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("John Doe", result.getContent().get(0).getName());
        assertEquals("jane@example.com", result.getContent().get(1).getEmail());

        // Verify the repository was called with the correct parameters
        verify(studentRepository).findByIsActive(0, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortField)));
    }

    
    @Test
     void testSoftDeleteStudent_StudentNotFound() {
        Long studentId = 2L;

        // Mock behavior for studentRepository to simulate a non-existing student
        when(studentRepository.existsById(studentId)).thenReturn(false);

        // Call the method under test and expect an exception
        assertThrows(UserNotFoundException.class, () -> {
            studentAccessor.softDeleteStudent(studentId, "admin123");
        });

        // Verify that the repository's methods were called as expected
        verify(studentRepository, times(1)).findById(studentId);
        // Verify that save was not called on audit repository
        verify(studentAuditRepository, never()).save(any());
    } 

    @Test
    void testSoftDeleteStudent_Success() throws UserNotFoundException {
        // Given
        Long studentId = 1L;
        String deletedBy = "admin123";
        when(studentRepository.existsById(studentId)).thenReturn(true);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student1));

        // When
        studentAccessor.softDeleteStudent(studentId, deletedBy);

        // Then
        verify(studentRepository, times(1)).save(any(Student.class)); // Ensure student is saved
        verify(studentAuditRepository, times(1)).save(any(StudentAudit.class)); // Ensure audit is saved
        assertEquals(1, student1.getIsActive()); // Check that the student is marked as inactive
    }
    


    @Test
     void testFindByEmail_Success() {
        // Arrange
        String email = "john@example.com";

        // Mock the behavior of studentRepository.findByEmail
        when(studentRepository.findByEmail(email)).thenReturn(student1);

        // Act
        Student result = studentAccessor.findByEmail(email);

        // Assert
        assertEquals(student1, result);
        verify(studentRepository).findByEmail(email); // Verify the interaction with the repository
    }
    
    @Test
    void testGetDeletedStudent() {
        // Given
        List<Student> deletedStudents = Arrays.asList(student1, student2);
        when(studentRepository.findByIsActive(1)).thenReturn(deletedStudents);

        // When
        List<Student> result = studentAccessor.getDeletedStudent();

        // Then
        assertEquals(2, result.size());
        verify(studentRepository, times(1)).findByIsActive(1);
    }

    @Test
    void testGetAllActiveStudents() {
        // Given
        List<Student> activeStudents = Arrays.asList(student1);
        when(studentRepository.findByIsActive(0)).thenReturn(activeStudents);

        // When
        List<Student> result = studentAccessor.getAllActiveStudents();

        // Then
        assertEquals(1, result.size());
        verify(studentRepository, times(1)).findByIsActive(0);
    }

    @Test
    void testGetPaginatedStudents() {
        // Given
        int page = 0;
        int size = 10;
        List<Student> students = Arrays.asList(student1, student2);
        Page<Student> studentPage = new PageImpl<>(students, PageRequest.of(page, size), students.size());
        when(studentRepository.findAll(PageRequest.of(page, size))).thenReturn(studentPage);

        // When
        Page<Student> result = studentAccessor.getPaginatedStudents(page, size);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(studentRepository, times(1)).findAll(PageRequest.of(page, size));
    }
    
    @Test
    void testRestoreStudent_Success() throws UserNotFoundException {
        // Given
        Long studentId = 1L;
        String restoredBy = "Admin";

        // Create a mock student and set its current state as deleted (isActive = 1)
        student1.setIsActive(1);

        // Mock the repository behavior
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student1));
        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        // Call the restoreStudent method
        studentAccessor.restoreStudent(studentId, restoredBy);

        // Assert that the student's isActive field was updated correctly
        assertEquals(0, student1.getIsActive());

        // Verify that the student was saved and the audit entry was created
        verify(studentRepository, times(1)).save(student1);
        verify(studentAuditRepository, times(1)).save(any(StudentAudit.class));
    }

    @Test
    void testRestoreStudent_StudentNotFound() {
        // Given
        Long studentId = 2L;
        String restoredBy = "Admin";

        // Mock the repository behavior to simulate student not found
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // Call the method and expect UserNotFoundException
        assertThrows(UserNotFoundException.class, () -> {
            studentAccessor.restoreStudent(studentId, restoredBy);
        });

        // Verify that no save operations were performed
        verify(studentRepository, never()).save(any(Student.class));
        verify(studentAuditRepository, never()).save(any(StudentAudit.class));
    }


}

