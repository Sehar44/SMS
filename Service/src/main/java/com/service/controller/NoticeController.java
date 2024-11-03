package com.service.controller;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.business.AdminService;
import com.example.business.CommentService;
import com.example.business.NoticeService;
import com.example.business.StudentService;
import com.example.data.Comment;
import com.example.data.LikesRepository;
import com.example.data.Notice;
import com.example.data.Student;
import com.example.exception.NoticeNotFoundException;

@Controller
@RequestMapping("/notices")
public class NoticeController {


	
	private final NoticeService noticeService;
	private final StudentService studentService;
	private final AdminService adminService;
	private final CommentService commentService;
	private final LikesRepository likesRepository;

	// Constructor with all dependencies injected
	@Autowired
	public NoticeController(NoticeService noticeService, 
	                        StudentService studentService, 
	                        AdminService adminService, 
	                        CommentService commentService, 
	                        LikesRepository likesRepository) {
	    this.noticeService = noticeService;
	    this.studentService = studentService;
	    this.adminService = adminService;
	    this.commentService = commentService;
	    this.likesRepository = likesRepository;
	}
	
	
	
	private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);
	private static final String REDIRECT_NOTICES = "redirect:/notices";


	@PostMapping("/save")
	public String saveNotice(@ModelAttribute Notice notice) throws NoticeNotFoundException {
		if (notice.getId() != null) {
			logger.info("Updating notice with ID: {}", notice.getId());
			noticeService.updateNotice(notice.getId(), notice);
		} else {
			logger.info("Saving new notice");
			noticeService.addNotice(notice);
		}
		logger.info("Notice saved/updated successfully");
		return REDIRECT_NOTICES;
	}

	@GetMapping("/edit/{id}")
	public String editNotice(@PathVariable("id") Long id, Model model) {
		 logger.info("Editing notice with ID: {}", id);
		Notice notice = noticeService.getNoticeById(id);
		model.addAttribute("notice", notice);
		model.addAttribute("notices", noticeService.getAllNotices());
		logger.info("Exiting editNotice");
		return "notice-page";
	}
	
	@PostMapping("/update/{id}")
	public String updateNotice(@PathVariable("id") Long id, @ModelAttribute Notice notice) throws NoticeNotFoundException {
	    logger.info("Updating notice with ID: {}", id);
	    noticeService.updateNotice(id, notice);
	    logger.info("Notice updated successfully");
	    return REDIRECT_NOTICES; // redirect to the notices list
	}

	@GetMapping("/delete/{id}")
	@Transactional
	public String deleteNotice(@PathVariable("id") Long id) throws NoticeNotFoundException {
		logger.info("Deleting notice with ID: {}", id);
		Notice notice = noticeService.findById(id);
		if (notice != null) {
			logger.info("Deleting associated comments and likes for notice ID: {}", id);
			commentService.deleteByNotice(notice.getId()); // Method to delete comments by notice
			likesRepository.deleteByNotice(notice); // Method to delete likes by notice
		}
		

		// Then, delete the notice
		noticeService.deleteNotice(id);
		logger.info("Notice with ID: {} deleted successfully", id);
		return REDIRECT_NOTICES;
	}



	// Helper methods to reduce duplication
    private void prepareNoticeModel(Long editId, Model model) {
        List<Notice> notices = noticeService.getAllNotices();
        Collections.reverse(notices); // Reverse to display from last to first
        model.addAttribute("notices", notices);

        if (editId != null) {
            Notice notice = noticeService.getNoticeById(editId);
            logger.info("Editing notice with ID: {}", editId);
            model.addAttribute("notice", notice);
        } else {
            logger.info("Creating new notice object for display.");
            model.addAttribute("notice", new Notice());
        }
    }

    private Notice getNoticeById(Long noticeId) {
        return noticeService.getNoticeById(noticeId);
    }

    private UserDetails getUserDetails(Authentication authentication) {
        return (UserDetails) authentication.getPrincipal();
    }

    private String getUserName(Authentication authentication, UserDetails userDetails) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return "Admin";
        } else {
            Student student = studentService.findByEmail(userDetails.getUsername());
            return student.getName();
        }
    }

    // Route methods
    @GetMapping("/studentNotice")
    public String showStudentNotice(@RequestParam(value = "editId", required = false) Long editId, Model model) {
        prepareNoticeModel(editId, model);
        logger.info("Exiting showStudentNotice");
        return "studentNotice";
    }

    @GetMapping()
    public String showNoticesPage(@RequestParam(value = "editId", required = false) Long editId, Model model) {
        prepareNoticeModel(editId, model);
        logger.info("Exiting showNoticesPage");
        return "notice-page";
    }

    @PostMapping("/react/{noticeId}")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> reactToNotice(@PathVariable("noticeId") Long noticeId,
                                                              @RequestParam("reaction") String reaction,
                                                              Authentication authentication) {

        Notice notice = getNoticeById(noticeId);
        UserDetails userDetails = getUserDetails(authentication);
        Student student = studentService.findByEmail(userDetails.getUsername());

        boolean hasReacted = noticeService.hasStudentReacted(student.getId(), noticeId);

        if (hasReacted) {
            return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
        }

        notice.setThumbsUp(notice.getThumbsUp() + 1);
        noticeService.addLike(student, notice);

        Map<String, Integer> response = new HashMap<>();
        response.put("thumbsUp", notice.getThumbsUp());
        return ResponseEntity.ok(response); // Return JSON response
    }

    @PostMapping("/comment/{noticeId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addComment(@PathVariable("noticeId") Long noticeId,
                                                          @RequestParam("comment") String commentText,
                                                          Authentication authentication) {

        if (commentText == null || commentText.trim().isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Comment cannot be empty.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Notice notice = getNoticeById(noticeId);
        UserDetails userDetails = getUserDetails(authentication);
        String userName = getUserName(authentication, userDetails);

        Comment comment = new Comment();
        comment.setNotice(notice);
        comment.setCommentText(commentText);

        if (userName.equals("Admin")) {
            comment.setAdmin(adminService.getAdminByUsername(userDetails.getUsername()));
        } else {
            comment.setStudent(studentService.findByEmail(userDetails.getUsername()));
        }

        commentService.addComment(comment);

        Map<String, Object> response = new HashMap<>();
        response.put("commentBy", userName);
        response.put("commentText", comment.getCommentText());
        response.put("createdAt", comment.getCreatedAt());

        return ResponseEntity.ok(response);
    }
}

