package edu.java.springsecuritytask.jwtbearerauth;

import edu.java.springsecuritytask.entity.User;
import edu.java.springsecuritytask.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

    private final UserRepository userRepository;

    public LogoutService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        org.springframework.security.core.userdetails.User authUser =  (org.springframework.security.core.userdetails.User)authentication.getPrincipal();

        User userFromDb = userRepository.findByUsername(authUser.getUsername()).orElseThrow(() -> new AuthenticationServiceException("Cannot find user in DB to clear token"));

        userFromDb.setToken("");

        userRepository.save(userFromDb);

    }
}
