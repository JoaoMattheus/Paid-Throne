package com.tronoremunerado.calculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.dao.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("PaidThrone Application Tests")
class PaidThroneApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        assertNotNull(jdbcTemplate, "JdbcTemplate should be initialized");
    }

    @Nested
    @DisplayName("Application Context Tests")
    class ApplicationContextTests {
        
        @Test
        @DisplayName("Should load application context successfully")
        void contextLoads() {
            assertNotNull(jdbcTemplate, "JdbcTemplate should be injected");
            assertDoesNotThrow(() -> jdbcTemplate.execute("SELECT 1"),
                    "Should be able to execute a simple query without throwing exception");
        }
    }

    @Nested
    @DisplayName("Database Connection Tests")
    class DatabaseConnectionTests {
        
        @Test
        @DisplayName("Should connect to database successfully")
        void shouldConnectToDatabase() {
            Boolean result = jdbcTemplate.queryForObject("SELECT TRUE", Boolean.class);
            assertTrue(result, "Should be able to execute a simple query");
        }

        @Test
        @DisplayName("Should handle database connection errors gracefully")
        void shouldHandleDatabaseConnectionErrors() {
            assertThrows(DataAccessException.class, () -> {
                jdbcTemplate.execute("SELECT invalid_column FROM non_existent_table");
            }, "Should throw exception for invalid query");
        }

        @Test
        @DisplayName("Should verify database is accessible")
        void shouldVerifyDatabaseAccess() {
            Integer tableCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema NOT IN ('information_schema', 'pg_catalog')", 
                Integer.class
            );
            assertNotNull(tableCount, "Should be able to query database metadata");
        }
    }
}
