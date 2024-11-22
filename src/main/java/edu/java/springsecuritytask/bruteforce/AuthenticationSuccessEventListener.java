package edu.java.springsecuritytask.bruteforce;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    BruteForceProtectionService bruteForceProtectionService;

    public AuthenticationSuccessEventListener(BruteForceProtectionService bruteForceProtectionService) {
        this.bruteForceProtectionService = bruteForceProtectionService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {

        bruteForceProtectionService.loginSuccess(event.getAuthentication().getName());

    }
}