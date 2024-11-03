package com.service.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.business.StudentService;
import com.example.data.Student;
import com.example.exception.InvalidAgeException;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.UserNotFoundException;
import com.service.config.Constants;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/students")
public class StudentController {
	

	private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;

    // Constructor Injection
    @Autowired
    public StudentController(StudentService studentService, PasswordEncoder passwordEncoder) {
        this.studentService = studentService;
        this.passwordEncoder = passwordEncoder;
    }
	 
	 private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

	
	 @Value("${file.upload-dir}")
	 private String uploadDir;
	 
	@GetMapping("/studentHome")
	public String studentHome() {
		logger.info("Accessed student home page.");
		return "student-home";
	}
	

	
	    @GetMapping("/gridData")
	    @ResponseBody
	    public Map<String, Object> getStudentGridData(
	            @RequestParam(defaultValue = Constants.DEFAULT_PAGE+"") int page, // jqGrid page numbers start from 1
	            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE+"") int rows, // Rows per page
	            @RequestParam(defaultValue = Constants.DEFAULT_SORT_COLUMN) String sidx, // Sort column (e.g. name, email, age)
	            @RequestParam(defaultValue = Constants.DEFAULT_SORT_ORDER) String sord, // Sort order (asc or desc)
	            @RequestParam(required = false) String _search, 
	            @RequestParam(required = false) String name,    // Search filter by name
	            @RequestParam(required = false) String email,   // Search filter by email
	            @RequestParam(required = false) String course) { // Search filter by course

	    	 logger.info("Fetching student grid data. Page: {}, Rows: {}, Sort By: {}, Sort Order: {}", page, rows, sidx, sord);
	    	    logger.debug("Search filters - Name: {}, Email: {}, Course: {}", name, email, course);

	        Page<Student> studentsPage = studentService.fetchFilteredStudents(page - 1, rows, sidx, sord, name, email, course);

	        Map<String, Object> response = new HashMap<>();
	        response.put("page", page);
	        response.put("total", studentsPage.getTotalPages());
	        response.put("records", studentsPage.getTotalElements());
	        response.put("rows", studentsPage.getContent());

	        

	        return response;
	    }
	


	// Paginated list of students
	@GetMapping("/all")
	public String getAllStudents(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, Model model) {

		logger.info("Fetching all students. Page: {}, Size: {}", page, size);
		
		Page<Student> studentsPage = studentService.fetchPaginatedStudents(page, size);
		model.addAttribute("students", studentsPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", studentsPage.getTotalPages());
		model.addAttribute("size", size);
		return "student-list";
	}

	// Handle form submission for adding a new student
	@PostMapping("/add")
	public String createStudent(@ModelAttribute Student student, RedirectAttributes redirectAttributes) throws UserAlreadyExistsException, InvalidAgeException {
		
		logger.info("Adding new student: {}", student);
		try {
		  student.setPassword(passwordEncoder.encode(student.getPassword()));
		  studentService.addStudent(student);
		  redirectAttributes.addFlashAttribute("successMessage", "Student added successfully!");
		} catch(UserAlreadyExistsException | InvalidAgeException e) {
			 redirectAttributes.addFlashAttribute("error", e.getMessage());
			
		} 
		
		return "redirect:/admin/addStudent";
		
	}
	
	
	
	
	// Handle deleting a student with pagination data
	@PostMapping("/hardDelete/{id}")
	public String deleteStudent(@PathVariable Long id, @RequestParam("page") int page, @RequestParam("size") int size) throws UserNotFoundException {
		studentService.deleteStudent(id);
		return "redirect:/students/all?page=" + page + "&size=" + size;
	}
	
	
	@PostMapping("/delete/{id}")
	public String softDeleteStudent(@PathVariable Long id, Authentication authentication,@RequestParam("page") int page, @RequestParam("size") int size) throws UserNotFoundException {
		String deletedBy= authentication.getName();
		logger.info("Soft deleting student. ID: {}, Deleted By: {}", id, deletedBy);
		studentService.softDeleteStudent(id,deletedBy);
		return "redirect:/students/all?page=" + page + "&size=" + size;
	}


	@PostMapping("/update")
	public ResponseEntity<Map<String, String>> updateStudent(@RequestParam("id") Long id,
	                                                         @RequestParam("name") String name,
	                                                         @RequestParam("email") String email,
	                                                         @RequestParam("age") Integer age,
	                                                         @RequestParam("course") String course) throws InvalidAgeException, UserNotFoundException {
		
		logger.info("Updating student. ID: {}, Name: {}, Email: {}, Age: {}, Course: {}", id, name, email, age, course);
	    Map<String, String> response = new HashMap<>();
	    try {
	        Student student = studentService.fetchStudentById(id);
	        student.setName(name);
	        student.setEmail(email);
	        student.setAge(age);
	        student.setCourse(course);
	        studentService.updateStudent(student.getId(), student);
	        
	        response.put("status", "success");
	        return ResponseEntity.ok(response);
	    } catch (UserAlreadyExistsException e) {
	        response.put("status", "error");
	        response.put("errorMessage", e.getMessage());
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // 409 Conflict
	    }
	    catch (InvalidAgeException e) {
	        response.put("status", "error");
	        response.put("errorMessage", e.getMessage());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 400 Bad Request
	    }
	    
	}

	@GetMapping("/Profile")
    public String showStudentProfile(Authentication authentication, Model model) {
		
		logger.info("Fetching student profile. Email: {}", authentication.getName());
        // Fetch the logged-in student's email (username)
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        // Fetch the student details from the database
        Student student = studentService.findByEmail(email);

        if(student.getProfileImagePath()==null) {
        	student.setProfileImagePath("uploads/default.png");
        }
        // Add student details to the model, excluding the password
        model.addAttribute("student", student);

        // Return profile view
        return "profile"; // Corresponding JSP page (profile.jsp)
    }
	
	

	
	@GetMapping("/exportToExcel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
	    logger.info("Exporting students to Excel");

	    // Set response headers
	    response.setContentType(Constants.EXCEL_CONTENT_TYPE);
	    response.setHeader(Constants.EXCEL_HEADER, Constants.EXCEL_FILENAME);

	    // Use try-with-resources to ensure the workbook is properly closed
	    try (Workbook workbook = new XSSFWorkbook()) {
	        Sheet sheet = workbook.createSheet("Students");

	        // Create a header row
	        Row header = sheet.createRow(0);
	        header.createCell(0).setCellValue("Name");
	        header.createCell(1).setCellValue("Email");
	        header.createCell(2).setCellValue("Age");
	        header.createCell(3).setCellValue("Course");

	        // Fetch data from the service layer
	        List<Student> students = studentService.fetchAllActiveStudents();
	        logger.debug("Number of students fetched for export: {}", students.size());

	        // Populate the sheet with student data
	        int rowNum = 1;
	        for (Student student : students) {
	            Row row = sheet.createRow(rowNum++);
	            row.createCell(0).setCellValue(student.getName());
	            row.createCell(1).setCellValue(student.getEmail());
	            row.createCell(2).setCellValue(student.getAge());
	            row.createCell(3).setCellValue(student.getCourse());
	        }

	        // Write the output to the response
	        workbook.write(response.getOutputStream());
	        logger.info("Students data successfully exported to Excel.");
	    } catch (IOException e) {
	        logger.error("Error occurred while exporting students to Excel: {}", e.getMessage());
	         // rethrow the exception to handle it in higher layers if needed
	    }
	}

	

	@PostMapping("/uploadProfileImage")
	public String uploadProfileImage(@RequestParam("profileImage") MultipartFile file, @RequestParam("studentId") Long studentId, RedirectAttributes redirectAttributes) throws UserAlreadyExistsException, InvalidAgeException, UserNotFoundException {
		
		logger.info("Uploading profile image. Student ID: {}", studentId);
	    // Ensure the directory exists
	    File uploadDirFile = new File(uploadDir);
	    if (!uploadDirFile.exists()) {
	        uploadDirFile.mkdirs();
	    }

	    // Validate file
	    if (file.isEmpty()) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Please select a valid file to upload.");
	        return "redirect:/students/Profile";
	    }

	    try {
	        // Save the file in the directory
	        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	        Path filePath = Paths.get(uploadDir + File.separator + fileName);
	        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	        // Save the file path in the student entity
	        Student student = studentService.fetchStudentById(studentId);
	        student.setProfileImagePath(Constants.UPLOAD_DIR + fileName);  // Store relative path
	        studentService.updateStudent(student.getId(), student);
	        
	        redirectAttributes.addFlashAttribute("successMessage", "Profile image uploaded successfully.");
	    } catch (IOException e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload the file.");
	        e.printStackTrace();
	    } 

	    return "redirect:/students/Profile";
	}

	@GetMapping("/deleted")
    public String getDeletedStudentsPage(Model model) {
        return "deletedList"; // Return the JSP view for deleted students
    }

    // Method to fetch deleted students as JSON
    @GetMapping("/deleted/list")
    @ResponseBody
    public List<Student> getDeletedStudents() {
        return studentService.getDeletedStudents(); // Fetch deleted students from the service
    }

    // Method to restore a deleted student
    @GetMapping("/restore/{id}")
    @ResponseBody
    public String restoreStudent(@PathVariable Long id, Authentication authentication) throws UserNotFoundException {
        String restoredBy= authentication.getName();
    	studentService.restoreStudent(id,restoredBy); // Call the service to restore the student
        return "Student restored successfully!";
    }
	


}
