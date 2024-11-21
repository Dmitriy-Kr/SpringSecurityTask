package edu.java.springsecuritytask.service;

import edu.java.springsecuritytask.bruteforce.BruteForceProtectionService;
import edu.java.springsecuritytask.entity.User;
import edu.java.springsecuritytask.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    BruteForceProtectionService bruteForceProtectionService;

    public AppUserDetailsService(UserRepository userRepository, BruteForceProtectionService bruteForceProtectionService) {
        this.userRepository = userRepository;
        this.bruteForceProtectionService = bruteForceProtectionService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, AuthenticationServiceException {
        if (bruteForceProtectionService.isBlocked(username)) {
            throw new AuthenticationServiceException("Blocked due to too many failed login attempts");
        }

        User userFromDB = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username %s not found".formatted(username)));

        return org.springframework.security.core.userdetails
                .User
                .builder()
                .username(userFromDB.getUsername())
                .password(userFromDB.getPassword())
                .authorities("USER")
                .disabled(!userFromDB.isActive())
                .build();
    }
}
