package com.example.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.accessor.StudentAccessor;
import com.example.data.Student;
import com.example.exception.InvalidAgeException;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.UserNotFoundException;

class StudentServiceTest {

    @Mock
    private StudentAccessor studentAccessor;

    @InjectMocks
    private StudentService studentService;

    private Student student1;
    private Student student2;
    private Page<Student> studentPage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create sample students
        student1 = new Student();
        student1.setId(1L);
        student1.setName("John Doe");
        student1.setEmail("john@example.com");
        student1.setCourse("CSE");

        student2 = new Student();
        student2.setId(2L);
        student2.setName("Jane Doe"); 
        student2.setEmail("jane@example.com");
        student2.setCourse("ECE");

        // Create a sample page of students
        studentPage = new PageImpl<>(Arrays.asList(student1, student2), PageRequest.of(0, 10), 2);
    }
 
    @Test
    void testFetchFilteredStudents() {
        // Mock the behavior of the accessor
        when(studentAccessor.fetchFilteredStudents(0, 10, "name", "asc", "John", "john@example.com", "CSE"))
                .thenReturn(studentPage);

        // Call the service method
        Page<Student> result = studentService.fetchFilteredStudents(0, 10, "name", "asc", "John", "john@example.com", "CSE");

        // Assertions 
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("John Doe", result.getContent().get(0).getName());

        // Verify the accessor method is called
        verify(studentAccessor, times(1)).fetchFilteredStudents(0, 10, "name", "asc", "John", "john@example.com", "CSE");
    }

    @Test
    void testFetchPaginatedStudents() {
        // Mock the behavior of the accessor
        when(studentAccessor.getPaginatedStudents(0, 10)).thenReturn(studentPage);

        // Call the service method
        Page<Student> result = studentService.fetchPaginatedStudents(0, 10);

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Jane Doe", result.getContent().get(1).getName());

        // Verify the accessor method is called
        verify(studentAccessor, times(1)).getPaginatedStudents(0, 10);
    }

    @Test
    void testFetchAllStudents() {
        // Mock the behavior of the accessor
        when(studentAccessor.getAllStudents()).thenReturn(Arrays.asList(student1, student2));

        // Call the service method
        List<Student> result = studentService.fetchAllStudents();

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());

        // Verify the accessor method is called
        verify(studentAccessor, times(1)).getAllStudents();
    }

    @Test
    void testFetchStudentById() {
        // Mock the behavior of the accessor
        when(studentAccessor.getStudentById(1L)).thenReturn(student1);

        // Call the service method
        Student result = studentService.fetchStudentById(1L);

        // Assertions
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());

        // Verify the accessor method is called
        verify(studentAccessor, times(1)).getStudentById(1L);
    }

    @Test
    void testAddStudent() throws UserAlreadyExistsException, InvalidAgeException {
        // Mock the behavior of the accessor
        when(studentAccessor.saveStudent(any(Student.class))).thenReturn(student1);

        // Call the service method
        Student savedStudent = studentService.addStudent(student1);

        // Assertions
        assertNotNull(savedStudent);
        assertEquals(1L, savedStudent.getId());
        assertEquals("John Doe", savedStudent.getName());

        // Verify the accessor method is called
        verify(studentAccessor, times(1)).saveStudent(student1);
    }

    @Test
    void testUpdateStudent() throws UserAlreadyExistsException, InvalidAgeException, UserNotFoundException {
        // Mock the behavior of the accessor
        when(studentAccessor.updateStudent(eq(1L), any(Student.class))).thenReturn(student1);

        // Call the service method
        Student updatedStudent = studentService.updateStudent(1L, student1);

        // Assertions
        assertNotNull(updatedStudent);
        assertEquals(1L, updatedStudent.getId());
        assertEquals("John Doe", updatedStudent.getName());

        // Verify the accessor method is called
        verify(studentAccessor, times(1)).updateStudent(eq(1L), any(Student.class));
    }

    @Test
    void testDeleteStudent() throws UserNotFoundException {
        // Call the service method
        studentService.deleteStudent(1L);

        // Verify the accessor method is called
        verify(studentAccessor, times(1)).deleteStudent(1L);
    }
    
    @Test
    void testFetchAllActiveStudents() {
        // Mock the behavior of the accessor
        when(studentAccessor.getAllActiveStudents()).thenReturn(Arrays.asList(student1, student2));

        // Call the service method
        List<Student> result = studentService.fetchAllActiveStudents();

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());

        // Verify the accessor method is called
        verify(studentAccessor, times(1)).getAllActiveStudents();
    }

    @Test
    void testFindByEmail() {
        // Mock the behavior of the accessor
        when(studentAccessor.findByEmail("john@example.com")).thenReturn(student1);

        // Call the service method
        Student result = studentService.findByEmail("john@example.com");

        // Assertions
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());

        // Verify the accessor method is called
        verify(studentAccessor, times(1)).findByEmail("john@example.com");
    }
    
    @Test
    void testSoftDeleteStudent() throws UserNotFoundException {
        // Call the service method
        studentService.softDeleteStudent(1L, "admin123");

        // Verify the accessor method is called
        verify(studentAccessor, times(1)).softDeleteStudent(1L, "admin123");
    }

    @Test
    void testRestoreStudent() throws UserNotFoundException {
        // Call the service method
        studentService.restoreStudent(1L,"admin123");

        // Verify the accessor method is called
        verify(studentAccessor, times(1)).restoreStudent(1L,"admin123");
    }

    @Test
    void testGetDeletedStudents() {
        // Mock the behavior of the accessor to return a list of deleted students
        when(studentAccessor.getDeletedStudent()).thenReturn(Arrays.asList(student1, student2));

        // Call the service method
        List<Student> result = studentService.getDeletedStudents();

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());

        // Verify the accessor method is called
        verify(studentAccessor, times(1)).getDeletedStudent();
    }
    
    
}
