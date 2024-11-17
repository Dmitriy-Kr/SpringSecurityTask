package edu.java.springsecuritytask.exception.handler;

import edu.java.springsecuritytask.exception.InvalidDataException;
import edu.java.springsecuritytask.exception.NoResourcePresentException;
import edu.java.springsecuritytask.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler(NoResourcePresentException.class)
    protected ResponseEntity<Object> handleNoResourcePresentException(NoResourcePresentException ex, WebRequest request) {
        return handleExceptionInternal(ex, getErrorResponse("The resource is not present in the database", ex), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ServiceException.class)
    protected ResponseEntity<Object> handleServiceException(ServiceException ex, WebRequest request) {
        return handleExceptionInternal(ex, getErrorResponse("Something gone wrong", ex), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(InvalidDataException.class)
    protected ResponseEntity<Object> handleServiceException(InvalidDataException ex, WebRequest request) {
        return handleExceptionInternal(ex, getErrorResponse("Invalid data", ex), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    private ErrorResponse getErrorResponse(String message, Throwable ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        logger.error(String.format("Error: %s, cause: %s", message, Arrays.toString(details.toArray())));

        return new ErrorResponse(message, details);
    }
}
