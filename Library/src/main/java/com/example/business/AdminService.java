package com.example.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.data.Admin;
import com.example.data.AdminRepository;

@Service
public class AdminService {
	
	
	private final AdminRepository adminRepository;
	
	@Autowired
	public AdminService(AdminRepository adminRepository) {
		this.adminRepository = adminRepository;
	}
	
	public Admin getAdminByUsername(String username) {
		return adminRepository.findByUsername(username);
	}

}
