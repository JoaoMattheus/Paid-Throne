package com.tronoremunerado.calculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PaidThroneApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Should load application context successfully")
    void contextLoads() {
        assertNotNull(jdbcTemplate, "JdbcTemplate should be injected");
    }

    @Test
    @DisplayName("Should connect to database successfully")
    void shouldConnectToDatabase() {
        Boolean result = jdbcTemplate.queryForObject("SELECT TRUE", Boolean.class);
        assertTrue(result, "Should be able to execute a simple query");
    }

}
