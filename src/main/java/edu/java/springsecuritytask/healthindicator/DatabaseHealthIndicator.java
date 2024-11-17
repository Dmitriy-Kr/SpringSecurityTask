package edu.java.springsecuritytask.healthindicator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component("MyDatabaseStatus")
public class DatabaseHealthIndicator implements HealthIndicator {
    private final DataSource dataSource;

    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1000)) {
                return Health.up().withDetail("Current Database", "Available").build();
            } else {
                return Health.down().withDetail("Current Database", "Unavailable").build();
            }
        } catch (SQLException e) {
            return Health.down(e).withDetail("Current Database", "Error").build();
        }
    }
}
