package edu.java.springsecuritytask.service;

import edu.java.springsecuritytask.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        edu.java.springsecuritytask.entity.User userFromDB = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username %s not found".formatted(username)));

        return User.builder()
                .username(userFromDB.getUsername())
                .password(userFromDB.getPassword())
                .authorities("USER")
                .disabled(!userFromDB.isActive())
                .build();
    }
}
