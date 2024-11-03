package com.example.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        // Initialize the Student objects
        student1 = new Student();
        student1.setName("John Doe");
        student1.setEmail("john@example.com");
        student1.setCourse("CSE");

        student2 = new Student();
        student2.setName("Jane Doe");
        student2.setEmail("jane@example.com");
        student2.setCourse("ECE");

        // Save them to the repository
        studentRepository.save(student1);
        studentRepository.save(student2);
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);

        // Test fetching all students
        Page<Student> result = studentRepository.findAll(pageable);

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("John Doe", result.getContent().get(0).getName());
        assertEquals("Jane Doe", result.getContent().get(1).getName());
    }

   

    @Test
    void testFindByEmail() {
        // Test finding by email
        Student result = studentRepository.findByEmail("john@example.com");

        // Assertions
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
    }

    @Test
    void testFindByName() {
        Pageable pageable = PageRequest.of(0, 10);

        // Test finding by name
        Page<Student> result = studentRepository.findByName("Jane Doe", pageable);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Jane Doe", result.getContent().get(0).getName());
    }

    @Test
    void testFindByCourse() {
        Pageable pageable = PageRequest.of(0, 10);

        // Test finding by course
        Page<Student> result = studentRepository.findByCourse("CSE", pageable);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("CSE", result.getContent().get(0).getCourse());
    }
}
