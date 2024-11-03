package com.service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import com.example.business.AdminService;
import com.example.business.CommentService;
import com.example.business.NoticeService;
import com.example.business.StudentService;
import com.example.data.Admin;
import com.example.data.Comment;
import com.example.data.LikesRepository;
import com.example.data.Notice;
import com.example.data.Student;
import com.example.exception.NoticeNotFoundException;

 class NoticeControllerTest {

    @InjectMocks
    private NoticeController noticeController;

    @Mock
    private NoticeService noticeService;

    @Mock
    private Model model;
    
    @Mock
    private StudentService studentService;

    @Mock
    private CommentService commentService;
    
    @Mock
    private AdminService adminService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;
    
    @Mock
    private LikesRepository likesRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testShowStudentNotice_WithEditId() {
        Long editId = 1L;
        Notice notice = new Notice();
        notice.setId(editId);
        notice.setSubject("Sample Notice");

        when(noticeService.getAllNotices()).thenReturn(Arrays.asList(notice));
        when(noticeService.getNoticeById(editId)).thenReturn(notice);

        String viewName = noticeController.showStudentNotice(editId, model);

        verify(model).addAttribute("notices", noticeService.getAllNotices());
        verify(model).addAttribute("notice", notice);
        assertEquals("studentNotice", viewName);
    }

    @Test
     void testShowStudentNotice_WithoutEditId() {
        // Arrange
        when(noticeService.getAllNotices()).thenReturn(Collections.emptyList());

        // Act
        String viewName = noticeController.showStudentNotice(null, model);

        // Assert
        verify(model).addAttribute("notices", Collections.emptyList()); // Expect empty notices list
        verify(model).addAttribute(eq("notice"), any(Notice.class)); // Verify new Notice object is added
        assertEquals("studentNotice", viewName);
    }

    @Test
     void testShowNoticesPage_WithEditId() {
        Long editId = 1L;
        Notice notice = new Notice();
        notice.setId(editId);
        notice.setSubject("Sample Notice");

        when(noticeService.getAllNotices()).thenReturn(Arrays.asList(notice));
        when(noticeService.getNoticeById(editId)).thenReturn(notice);

        String viewName = noticeController.showNoticesPage(editId, model);

        verify(model).addAttribute("notices", noticeService.getAllNotices());
        verify(model).addAttribute("notice", notice);
        assertEquals("notice-page", viewName);
    }
    @Test
     void testShowNoticesPage_WithoutEditId() {
        // Arrange
        when(noticeService.getAllNotices()).thenReturn(Collections.emptyList());

        // Act
        String viewName = noticeController.showNoticesPage(null, model);

        // Assert
        InOrder inOrder = inOrder(model);
        
        // Verify the order of invocations
        inOrder.verify(model).addAttribute("notices", Collections.emptyList());
        inOrder.verify(model).addAttribute(eq("notice"), any(Notice.class));

        assertEquals("notice-page", viewName);
    }

    @Test
     void testSaveNotice_Create() throws NoticeNotFoundException {
        Notice notice = new Notice();
        notice.setId(null); // New notice

        noticeController.saveNotice(notice);

        verify(noticeService).addNotice(notice);
        verify(noticeService, never()).updateNotice(any(Long.class), any(Notice.class));
    }

    @Test
    void testSaveNotice_Update() throws NoticeNotFoundException {
        Notice notice = new Notice();
        notice.setId(1L); // Existing notice

        noticeController.saveNotice(notice);

        verify(noticeService).updateNotice(1L, notice); // Pass the values directly
        verify(noticeService, never()).addNotice(any(Notice.class));
    }
    @Test
     void testEditNotice() {
        Long id = 1L;
        Notice notice = new Notice();
        notice.setId(id);
        notice.setSubject("Sample Notice");

        when(noticeService.getNoticeById(id)).thenReturn(notice);
        when(noticeService.getAllNotices()).thenReturn(Arrays.asList(notice));

        String viewName = noticeController.editNotice(id, model);

        verify(model).addAttribute("notice", notice);
        verify(model).addAttribute("notices", noticeService.getAllNotices());
        assertEquals("notice-page", viewName);
    }

    @Test
    void testDeleteNotice() throws NoticeNotFoundException {
        // Arrange
        Long id = 1L;
        Notice notice = new Notice();  // Mocking a non-null Notice object
        notice.setId(id);
        
        when(noticeService.findById(id)).thenReturn(notice);

        // Act
        String viewName = noticeController.deleteNotice(id);

        // Assert
        verify(commentService).deleteByNotice(id);  // Verifying the associated methods are called
        verify(likesRepository).deleteByNotice(notice);
        verify(noticeService).findById(id);  // Verifying the notice is fetched
        assertEquals("redirect:/notices", viewName);  // Verifying the return value
    }
    
    @Test
    void testReactToNotice_ValidReaction() {
       Long noticeId = 1L;
       Notice notice = new Notice();
       notice.setId(noticeId);
       notice.setThumbsUp(0);

       Student student = new Student();
       student.setId(1L);

       when(noticeService.getNoticeById(noticeId)).thenReturn(notice);
       when(authentication.getPrincipal()).thenReturn(userDetails);
       when(userDetails.getUsername()).thenReturn("student@example.com");
       when(studentService.findByEmail("student@example.com")).thenReturn(student);
       when(noticeService.hasStudentReacted(1L, noticeId)).thenReturn(false);

       ResponseEntity<Map<String, Integer>> response = noticeController.reactToNotice(noticeId, "thumbsUp", authentication);

       verify(noticeService).addLike(student, notice);
       assertEquals(1, notice.getThumbsUp());
       assertEquals(HttpStatus.OK, response.getStatusCode());
       assertEquals(1, response.getBody().get("thumbsUp"));
   }
    
    @Test
    void testReactToNotice_StudentAlreadyReacted() {
       Long noticeId = 1L;
       Student student = new Student();
       student.setId(1L);

       when(authentication.getPrincipal()).thenReturn(userDetails);
       when(userDetails.getUsername()).thenReturn("student@example.com");
       when(studentService.findByEmail("student@example.com")).thenReturn(student);
       when(noticeService.hasStudentReacted(1L, noticeId)).thenReturn(true);

       ResponseEntity<Map<String, Integer>> response = noticeController.reactToNotice(noticeId, "thumbsUp", authentication);

       verify(noticeService, never()).addLike(any(), any());
       assertEquals(400, response.getStatusCode().value());
   }
    
    @Test
    void testAddComment_ValidComment_Student() {
       Long noticeId = 1L;
       Notice notice = new Notice();
       notice.setId(noticeId);
       Student student = new Student();
       student.setName("John Doe");

       when(noticeService.getNoticeById(noticeId)).thenReturn(notice);
       when(authentication.getPrincipal()).thenReturn(userDetails);
       when(userDetails.getUsername()).thenReturn("student@example.com");
       when(studentService.findByEmail("student@example.com")).thenReturn(student);

       Comment comment = new Comment();
       comment.setNotice(notice);
       comment.setCommentText("Great Notice!");
       comment.setStudent(student);

       ResponseEntity<Map<String, Object>> response = noticeController.addComment(noticeId, "Great Notice!", authentication);

       verify(commentService).addComment(any(Comment.class));
       assertEquals(200, response.getStatusCode().value());
       assertEquals("John Doe", response.getBody().get("commentBy"));
       assertEquals("Great Notice!", response.getBody().get("commentText"));
   }

   @Test
    void testAddComment_EmptyComment() {
       Long noticeId = 1L;

       ResponseEntity<Map<String, Object>> response = noticeController.addComment(noticeId, "", authentication);

       verify(commentService, never()).addComment(any());
       assertEquals(400, response.getStatusCode().value());
       assertEquals("Comment cannot be empty.", response.getBody().get("error"));
   }
   
   @Test
   void testAddComment_AsAdmin() {
       // Arrange
       SecurityContext securityContext = new SecurityContext() {
           @Override
           public Authentication getAuthentication() {
               return new Authentication() {
                   @Override
                   public Collection<? extends GrantedAuthority> getAuthorities() {
                       Collection<GrantedAuthority> authorities = new ArrayList<>();
                       authorities.add(() -> "ROLE_ADMIN"); // Lambda for GrantedAuthority
                       return authorities;
                   }

                   @Override
                   public Object getCredentials() {
                       return null;
                   }

                   @Override
                   public Object getDetails() {
                       return null;
                   }

                   @Override
                   public Object getPrincipal() {
                       return new UserDetails() {
                           @Override
                           public Collection<? extends GrantedAuthority> getAuthorities() {
                               return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
                           }

                           @Override
                           public String getPassword() {
                               return "password";
                           }

                           @Override
                           public String getUsername() {
                               return "admin123";
                           }

                           @Override
                           public boolean isAccountNonExpired() {
                               return true;
                           }

                           @Override
                           public boolean isAccountNonLocked() {
                               return true;
                           }

                           @Override
                           public boolean isCredentialsNonExpired() {
                               return true;
                           }

                           @Override
                           public boolean isEnabled() {
                               return true;
                           }
                       };
                   }

                   @Override
                   public boolean isAuthenticated() {
                       return true;
                   }

                   @Override
                   public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
                	   throw new UnsupportedOperationException("setAuthentication is not supported in this context.");
                   }

                   @Override
                   public String getName() {
                       return "admin123";
                   }
               };
           }

           public void setAuthentication(Authentication authentication) {
        	   throw new UnsupportedOperationException("setAuthentication is not supported in this context.");
        	   
            
           }
       };

       // Set the mocked security context
       SecurityContextHolder.setContext(securityContext);

       // Mock data and method calls
       String commentText = "This is a comment";
       Long noticeId = 1L;

       Notice notice = new Notice();
       notice.setId(noticeId);

       Admin admin = new Admin();
       admin.setUsername("admin123");

       // Mock service calls
       when(noticeService.getNoticeById(noticeId)).thenReturn(notice);
       when(adminService.getAdminByUsername("admin123")).thenReturn(admin);

       // Perform the POST request
       ResponseEntity<Map<String, Object>> response = noticeController.addComment(noticeId, commentText, securityContext.getAuthentication());

       // Assertions
       assertEquals(HttpStatus.OK, response.getStatusCode());
       assertEquals("Admin", response.getBody().get("commentBy"));
       assertEquals(commentText, response.getBody().get("commentText"));
   }
   
   @Test
   void testUpdateNotice() throws NoticeNotFoundException {
       // Arrange
       Long noticeId = 1L;
       Notice updatedNotice = new Notice();
       updatedNotice.setId(noticeId);
       updatedNotice.setSubject("Updated Notice Subject");

       // Act
       String result = noticeController.updateNotice(noticeId, updatedNotice);

       // Assert
       verify(noticeService).updateNotice(noticeId, updatedNotice);
       assertEquals("redirect:/notices", result);
   }

}
