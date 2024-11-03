package com.example.data;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{

	 Page<Student> findAll(Pageable pageable); 
	 
	 @Query("SELECT s FROM Student s WHERE "
		        + "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND "
		        + "(:email IS NULL OR LOWER(s.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND "
		        + "(:course IS NULL OR LOWER(s.course) LIKE LOWER(CONCAT('%', :course, '%'))) AND "
		        + "(s.isActive = 0)")
		Page<Student> findByFilters(@Param("name") String name,
		                            @Param("email") String email,
		                            @Param("course") String course,
		                            Pageable pageable);
		

	     Student findByEmail(String username);

		Page<Student> findByName(String name, Pageable pageable);

		Page<Student> findByEmail(String email, Pageable pageable);

		Page<Student> findByCourse(String course, Pageable pageable);
	
		 Page<Student> findByIsActive(Integer isActive, Pageable pageable);
		 
		 List<Student> findByIsActive(Integer isActive);
		 
		 
	 
}
