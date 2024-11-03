package com.example.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, String>{

	Admin findByUsername(String username);
}
