package com.example.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAuditRepository extends JpaRepository<StudentAudit, Long>{

}
