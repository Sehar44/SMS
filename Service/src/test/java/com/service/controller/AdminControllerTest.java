package com.service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureDataJpa
@ActiveProfiles("test")
 class AdminControllerTest {


	  @Autowired
	    private AdminController adminController;

	    private MockMvc mockMvc;

	    @BeforeEach
	    public void setUp() {
	        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
	    }

	    @Test
	     void testAdminHome()  {

	    	String viewName = adminController.adminHome();
	    	assertEquals("admin-home", viewName);
	    }

	    @Test
	     void testAddStudent() throws Exception {
	        mockMvc.perform(get("/admin/addStudent"))
	                .andExpect(view().name("add-student"));
	    }
}
