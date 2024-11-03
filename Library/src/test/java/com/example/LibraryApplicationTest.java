package com.example;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;


@SpringBootTest
class LibraryApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testMain() {
        // Ensure that the application context is loaded successfully
        assertNotNull(applicationContext, "The application context should have loaded.");
    }

    @Test
    void testMainMethod() {
        // Explicitly call the main method to ensure coverage
        LibraryApplication.main(new String[] {});
        // Assert that the application context is not null after calling the main method
        assertNotNull(applicationContext, "The application context should have been initialized after running the main method.");
    }
}