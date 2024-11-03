package com.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@GetMapping("/login")
    public String showLoginPage() {
		
		logger.info("Login page requested");
        return "login"; // Render the login.jsp page
    }

    @GetMapping("/default")
    public String defaultAfterLogin() {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
        	logger.info("Admin login detected, redirecting to admin home");
            return "redirect:/admin/home";
        }else {
        	logger.info("Non-admin login detected, redirecting to student home");
        	
        }
        return "redirect:/students/studentHome";
    }
	
}
