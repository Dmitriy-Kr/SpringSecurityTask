package edu.java.springsecuritytask.healthindicator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("CheckRemoteService")
public class ThisServiceHealthIndicator implements HealthIndicator {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String REMOTE_SERVICE_URL = "https://remote-service/health";

    @Override
    public Health health() {
        try {
            String response = restTemplate.getForObject(REMOTE_SERVICE_URL, String.class);
            if ("OK".equalsIgnoreCase(response)) {
                return Health.up().withDetail("RemoteService", "Available").build();
            } else {
                return Health.down().withDetail("RemoteService", "Unavailable").build();
            }
        } catch (Exception e) {
            return Health.down(e).withDetail("RemoteService", "Error").build();
        }
    }
}
