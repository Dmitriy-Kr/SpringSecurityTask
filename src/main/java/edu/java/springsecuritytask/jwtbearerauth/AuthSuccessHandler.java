package edu.java.springsecuritytask.jwtbearerauth;

import edu.java.springsecuritytask.entity.User;
import edu.java.springsecuritytask.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private UserRepository userRepository;
    private JwtTokenService jwtTokenService;

    private static Logger logger = LoggerFactory.getLogger(AuthSuccessHandler.class);

    public AuthSuccessHandler(UserRepository userRepository, JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        org.springframework.security.core.userdetails.User authUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User userFromDB = userRepository.findByUsername(authUser.getUsername())
                .orElseThrow(() -> new AuthenticationServiceException("Cannot create token - no username in DB"));

        String jwtToken = jwtTokenService.getJwtToken(userFromDB);

        userFromDB.setToken(jwtToken);

        userRepository.save(userFromDB);

        response.setStatus(HttpServletResponse.SC_OK);

        try (PrintWriter out = response.getWriter()) {

            response.setContentType("text/html;charset=UTF-8");

            out.print("JWTToken: ");
            out.print(jwtToken);

        } catch (Exception ex){
            logger.error("An error occurred while creating token", ex);
            throw new AuthenticationServiceException(ex.getMessage());
        }

    }
}
