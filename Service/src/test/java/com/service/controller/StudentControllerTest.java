package com.service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import com.example.business.StudentService;
import com.example.data.AdminRepository;
import com.example.data.Student;
import com.example.exception.InvalidAgeException;
import com.example.exception.UserAlreadyExistsException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

 class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private StudentController studentController;
    

    @Mock
    private Logger logger;

    private MockMvc mockMvc;
    
    @Value("${file.upload-dir:/tmp/uploads}")
    private String uploadDir; 

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
        ReflectionTestUtils.setField(studentController, "uploadDir", "/mock/upload/dir");
    }

    @Test
     void testStudentHome() throws Exception {
        mockMvc.perform(get("/students/studentHome"))
               .andExpect(status().isOk())
               .andExpect(view().name("student-home"));
    }

    @Test
     void testGetAllStudents() throws Exception {
        Page<Student> studentPage = new PageImpl<>(
                Collections.singletonList(new Student(1L, "John Doe", "john@example.com", 22, "CSE")));
        when(studentService.fetchPaginatedStudents(anyInt(), anyInt())).thenReturn(studentPage);

        mockMvc.perform(get("/students/all").param("page", "0").param("size", "10"))
               .andExpect(status().isOk())
               .andExpect(view().name("student-list"))
               .andExpect(model().attributeExists("students"))
               .andExpect(model().attribute("currentPage", 0))
               .andExpect(model().attribute("totalPages", studentPage.getTotalPages()));
    }

    @Test
     void testCreateStudent() throws Exception {
        Student student = new Student(1L, "John Doe", "john@example.com", 22, "CSE");
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        mockMvc.perform(post("/students/add")
                        .flashAttr("student", student))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/admin/addStudent"));
    }

    @Test
     void testGetStudentGridData() throws Exception {
        Page<Student> studentPage = new PageImpl<>(
                Collections.singletonList(new Student(1L, "John Doe", "john@example.com", 22, "CSE")));
        when(studentService.fetchFilteredStudents(anyInt(), anyInt(), anyString(), anyString(), anyString(),
                anyString(), anyString())).thenReturn(studentPage);

        mockMvc.perform(get("/students/gridData")
                        .param("page", "1")
                        .param("rows", "10")
                        .param("sidx", "name")
                        .param("sord", "asc")
                        .param("name", "John Doe")
                        .param("email", "john@example.com")
                        .param("course", "CSE")
                        .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }


    
    @Test
    void testUpdateStudent() throws Exception {
        Long studentId = 1L;
        String updatedName = "John Doe";
        String updatedEmail = "john.doe@example.com";
        Integer updatedAge = 20;
        String updatedCourse = "Computer Science";

        // Mock the behavior of studentService
        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setName("Old Name");
        existingStudent.setEmail("old.email@example.com");
        existingStudent.setAge(19);
        existingStudent.setCourse("Old Course");

        when(studentService.fetchStudentById(studentId)).thenReturn(existingStudent);

        // Perform the update request
        mockMvc.perform(post("/students/update")
                .param("id", String.valueOf(studentId))
                .param("name", updatedName)
                .param("email", updatedEmail)
                .param("age", String.valueOf(updatedAge))
                .param("course", updatedCourse)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())  // Expect 200 OK
                .andExpect(jsonPath("$.status").value("success"));  // Check for the "status": "success" in the JSON response

        // Verify that the update method was called with the updated student details
        verify(studentService).updateStudent(eq(studentId), any(Student.class));
    }
    
    


    @Test
    void testUpdateStudent_UserAlreadyExistsException() throws Exception {
        Long studentId = 1L;
        String updatedName = "John Doe";
        String updatedEmail = "john.doe@example.com";
        Integer updatedAge = 20;
        String updatedCourse = "Computer Science";

        // Mock the behavior of studentService.fetchStudentById to return an existing student
        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setName(updatedName);
        existingStudent.setEmail(updatedEmail);
        existingStudent.setAge(updatedAge);
        existingStudent.setCourse(updatedCourse);

        when(studentService.fetchStudentById(studentId)).thenReturn(existingStudent);

        // Mock behavior: throw UserAlreadyExistsException when updateStudent is called
        doThrow(new UserAlreadyExistsException("User with email already exists")).when(studentService).updateStudent(eq(studentId), any(Student.class));

        // Perform the update request
        mockMvc.perform(post("/students/update")
                .param("id", String.valueOf(studentId))
                .param("name", updatedName)
                .param("email", updatedEmail)
                .param("age", String.valueOf(updatedAge))
                .param("course", updatedCourse)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isConflict())  // Expect 409 Conflict
                .andExpect(jsonPath("$.status").value("error"))  // Check for "status": "error"
                .andExpect(jsonPath("$.errorMessage").value("User with email already exists"));  // Check error message

        // Verify that the update method was called with the correct arguments
        verify(studentService).updateStudent(eq(studentId), any(Student.class));
    }


    @Test
    void testUpdateStudent_InvalidAgeException() throws Exception {
        Long studentId = 1L;
        String updatedName = "John Doe";
        String updatedEmail = "john.doe@example.com";
        Integer updatedAge = 10;  // Invalid age
        String updatedCourse = "Computer Science";

        // Mock behavior: fetch the student first
        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setName(updatedName);
        existingStudent.setEmail(updatedEmail);
        existingStudent.setAge(updatedAge); // Invalid age set
        existingStudent.setCourse(updatedCourse);

        // When fetching student, return the existingStudent object
        when(studentService.fetchStudentById(studentId)).thenReturn(existingStudent);

        // Mock behavior: throw InvalidAgeException when update is attempted
        doThrow(new InvalidAgeException("Invalid age provided")).when(studentService).updateStudent(eq(studentId), any(Student.class));

        // Perform the update request
        mockMvc.perform(post("/students/update")
                .param("id", String.valueOf(studentId))
                .param("name", updatedName)
                .param("email", updatedEmail)
                .param("age", String.valueOf(updatedAge))  // Invalid age
                .param("course", updatedCourse)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())  // Expect 400 Bad Request
                .andExpect(jsonPath("$.status").value("error"))  // Check for "status": "error"
                .andExpect(jsonPath("$.errorMessage").value("Invalid age provided"));  // Check error message

        // Verify that the update method was attempted
        verify(studentService).updateStudent(eq(studentId), any(Student.class));
    }


    @Test
     void testShowStudentProfile() throws Exception {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("john@example.com");

        Student student = new Student(1L, "John Doe", "john@example.com", 22, "CSE");
        when(studentService.findByEmail(anyString())).thenReturn(student);

        mockMvc.perform(get("/students/Profile").principal(authentication))
               .andExpect(status().isOk())
               .andExpect(view().name("profile"))
               .andExpect(model().attributeExists("student"));
    }
    
    @Test
    void testHardDeleteStudent() throws Exception {
        Long studentId = 1L;

        // Mock the behavior of studentService.deleteStudent
        doNothing().when(studentService).deleteStudent(studentId);

        // Perform the delete request
        mockMvc.perform(post("/students/hardDelete/{id}", studentId)
                        .param("page", "0")
                        .param("size", "10"))
               .andExpect(status().is3xxRedirection())  // Expect redirection after deletion
               .andExpect(view().name("redirect:/students/all?page=0&size=10"));

        // Verify that the delete method was called with the correct ID
        verify(studentService).deleteStudent(studentId);
    }



    @Test
    void testSoftDeleteStudent() throws Exception {
        Long studentId = 1L;
        String deletedBy = "admin123";  // Mocked authenticated username

        // Mock the behavior of authentication and studentService
        when(authentication.getName()).thenReturn(deletedBy);
        doNothing().when(studentService).softDeleteStudent(studentId, deletedBy);

        // Perform the soft delete request
        mockMvc.perform(post("/students/delete/{id}", studentId)
                        .principal(authentication)
                        .param("page", "0")
                        .param("size", "10"))
               .andExpect(status().is3xxRedirection())  // Expect redirection after soft deletion
               .andExpect(view().name("redirect:/students/all?page=0&size=10"));

        // Verify that the soft delete method was called with the correct ID and deletedBy
        verify(studentService).softDeleteStudent(studentId, deletedBy);
    }
    
    // Test for restoring a deleted student
    @Test
    void testRestoreStudent() throws Exception {
        Long studentId = 1L;  // ID of the student to restore
        String restoredBy = "admin123";  // Mocked authenticated username

        // Mock the behavior of authentication and studentService
        when(authentication.getName()).thenReturn(restoredBy);
        doNothing().when(studentService).restoreStudent(studentId, restoredBy);

        // Perform the restore request
        mockMvc.perform(get("/students/restore/{id}", studentId)
                        .principal(authentication))  // Pass the mocked authentication
                .andExpect(status().isOk())  // Expect HTTP 200 OK
                .andExpect(content().string("Student restored successfully!"));  // Check the response content

        // Verify that the restore method was called with the correct ID and restoredBy
        verify(studentService).restoreStudent(studentId, restoredBy);
    }

   
    
    @Test
    void testGetDeletedStudentsPage() throws Exception {
        mockMvc.perform(get("/students/deleted"))
                .andExpect(status().isOk()) // Expecting HTTP 200 OK
                .andExpect(view().name("deletedList")); // The view name should be 'deletedList'
    }

    // Test for getDeletedStudents (returns JSON response)
    @Test
    void testGetDeletedStudents() throws Exception {
        // Mock the deleted students returned from the service
        List<Student> deletedStudents = Arrays.asList(
                new Student(1L, "John Doe", "john@example.com", 22, "CSE"),
                new Student(2L, "Jane Doe", "jane@example.com", 21, "ECE")
        );
        when(studentService.getDeletedStudents()).thenReturn(deletedStudents);

        // Perform the GET request to /students/deleted/list
        mockMvc.perform(get("/students/deleted/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expecting HTTP 200 OK
                .andExpect(jsonPath("$[0].id").value(1)) // First student's ID is 1
                .andExpect(jsonPath("$[0].name").value("John Doe")) // First student's name
                .andExpect(jsonPath("$[1].id").value(2)) // Second student's ID is 2
                .andExpect(jsonPath("$[1].name").value("Jane Doe")); // Second student's name
    }
    

    @Test
    void testExportToExcel_IOException() throws Exception {
        // Mocking the student service to return a list of active students
        List<Student> students = Arrays.asList(
                new Student(1L, "John Doe", "john@example.com", 22, "CSE"),
                new Student(2L, "Jane Doe", "jane@example.com", 21, "ECE")
        );
        when(studentService.fetchAllActiveStudents()).thenReturn(students);

        // Mock the response's OutputStream to throw an IOException when writing
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletOutputStream outputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(outputStream);
        doThrow(new IOException("Test IOException")).when(outputStream).write(any(byte[].class), anyInt(), anyInt());

        // Perform the GET request to export to Excel and verify the exception is handled
        mockMvc.perform(get("/students/exportToExcel"))
                .andExpect(status().isOk());

    }


    // Test for uploadProfileImage
    @Test
    void testUploadProfileImageSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile("profileImage", "test-image.jpg", "image/jpeg", "test data".getBytes());

        // Set up mock student
        Student student = new Student();
        student.setId(1L);
        student.setName("John Doe");

        // Mock student service call
        when(studentService.fetchStudentById(1L)).thenReturn(student);

        mockMvc.perform(multipart("/students/uploadProfileImage")
                .file(file)
                .param("studentId", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("successMessage", "Profile image uploaded successfully."))
            .andReturn();
    }

    @Test
    void testUploadProfileImageFail_emptyFile() throws Exception {
        // Mock student data
        Student student = new Student(1L, "John Doe", "john@example.com", 22, "CSE");
        when(studentService.fetchStudentById(1L)).thenReturn(student);

        // Mock an empty file upload
        MockMultipartFile emptyFile = new MockMultipartFile(
                "profileImage", 
                "", 
                "image/jpeg", 
                new byte[0]
        );

        // Perform the POST request with an empty file
        mockMvc.perform(multipart("/students/uploadProfileImage")
                        .file(emptyFile)
                        .param("studentId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students/Profile"))
                .andExpect(flash().attribute("errorMessage", "Please select a valid file to upload."));
    }

   
    
}


