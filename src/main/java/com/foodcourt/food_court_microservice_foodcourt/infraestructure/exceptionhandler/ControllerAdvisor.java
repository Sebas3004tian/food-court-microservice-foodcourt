package com.foodcourt.food_court_microservice_foodcourt.infraestructure.exceptionhandler;

import com.foodcourt.food_court_microservice_foodcourt.domain.exception.*;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAdvisor.class);

    private static final String MESSAGE = "message";
    private static final String ERROR = "error";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.error("Validation error: {}", ex.getMessage(), ex);
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            fieldErrors.put(field, message);
        });

        Map<String, Object> response = new HashMap<>();
        response.put(ERROR, ExceptionResponse.VALIDATION_ERROR.getMessage());
        response.put(MESSAGE, fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        logger.error("AccessDeniedException: {}", ex.getMessage(), ex);
        Map<String, String> response = Map.of(
                ERROR, ExceptionResponse.ACCESS_DENIED.getMessage(),
                MESSAGE, ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
    @ExceptionHandler(SecurityConfigurationException.class)
    public ResponseEntity<Map<String, String>> handleSecurityConfigurationException(SecurityConfigurationException ex) {
        logger.error("SecurityConfigurationException: {}", ex.getMessage(), ex);
        Map<String, String> response = Map.of(
                ERROR, ExceptionResponse.SECURITY_CONFIGURATION_ERROR.getMessage(),
                MESSAGE, ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidUserRoleException.class)
    public ResponseEntity<Map<String, String>> handleUserRoleException(InvalidUserRoleException ex) {
        logger.error("InvalidUserRoleException: {}", ex.getMessage(), ex);
        Map<String, String> response = Map.of(
                ERROR, ExceptionResponse.USER_ROLE_ERROR.getMessage(),
                MESSAGE, ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<Map<String, String>> handleUserServiceException(UserServiceException ex) {
        logger.error("UserServiceException: {}", ex.getMessage(), ex);
        Map<String, String> response = Map.of(
                ERROR, ExceptionResponse.USER_MICROSERVICE_ERROR.getMessage(),
                MESSAGE, ex.getMessage()
        );

        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        logger.error("CategoryNotFoundException: {}", ex.getMessage(), ex);
        Map<String, String> response = Map.of(
                ERROR, ExceptionResponse.DATA_NOT_FOUND.getMessage(),
                MESSAGE, ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    @ExceptionHandler(DishNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleDishNotFoundException(DishNotFoundException ex) {
        logger.error("DishNotFoundException: {}", ex.getMessage(), ex);
        Map<String, String> response = Map.of(
                ERROR, ExceptionResponse.DATA_NOT_FOUND.getMessage(),
                MESSAGE, ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        logger.error("EmployeeNotFoundException: {}", ex.getMessage(), ex);
        Map<String, String> response = Map.of(
                ERROR, ExceptionResponse.DATA_NOT_FOUND.getMessage(),
                MESSAGE, ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotFoundException(OrderNotFoundException ex) {
        logger.error("OrderNotFoundException: {}", ex.getMessage(), ex);
        Map<String, String> response = Map.of(
                ERROR, ExceptionResponse.DATA_NOT_FOUND.getMessage(),
                MESSAGE, ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRestaurantNotFoundException(RestaurantNotFoundException ex) {
        logger.error("RestaurantNotFoundException: {}", ex.getMessage(), ex);
        Map<String, String> response = Map.of(
                ERROR, ExceptionResponse.DATA_NOT_FOUND.getMessage(),
                MESSAGE, ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedException(UnauthorizedException ex) {
        logger.error("UnauthorizedException: {}", ex.getMessage(), ex);
        Map<String, String> response = Map.of(
                ERROR, ExceptionResponse.UNAUTHORIZED_ERROR.getMessage(),
                MESSAGE, ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleAlreadyExistException(AlreadyExistsException ex) {
        logger.error("AlreadyExistsException: {}", ex.getMessage(), ex);
        Map<String, String> response = Map.of(
                ERROR, ExceptionResponse.ALREADY_EXISTS.getMessage(),
                MESSAGE, ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(DishStatusAlreadySetException.class)
    public ResponseEntity<Map<String, String>> handleDishStatusAlreadySetException(DishStatusAlreadySetException ex) {
        logger.error("DishStatusAlreadySetException: {}", ex.getMessage(), ex);
        Map<String, String> response = Map.of(
                ERROR, ExceptionResponse.DISH_STATUS_ALREADY_SET.getMessage(),
                MESSAGE, ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("IllegalArgumentException: {}", ex.getMessage(), ex);
        Map<String, String> response = Map.of(
                ERROR, ExceptionResponse.ILLEGAL_ARGUMENT_ERROR.getMessage(),
                MESSAGE, ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<Map<String, String>> handleInvalidOrderStatusException(InvalidOrderStatusException ex) {
        logger.error("InvalidOrderStatusException: {}", ex.getMessage(), ex);
        Map<String, String> response = Map.of(
                ERROR, ExceptionResponse.INVALID_ORDER_STATUS_ERROR.getMessage(),
                MESSAGE, ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ClientHasActiveOrderException.class)
    public ResponseEntity<Map<String, String>> handleClientHasActiveOrderException(ClientHasActiveOrderException ex) {
        logger.error("ClientHasActiveOrderException: {}", ex.getMessage(), ex);
        Map<String, String> response = Map.of(
                ERROR, ExceptionResponse.CLIENT_HAS_ACTIVE_ORDER.getMessage(),
                MESSAGE, ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<Map<String, String>> handleExternalServiceException(ExternalServiceException ex) {
        logger.error("ExternalServiceException: {}", ex.getMessage(), ex);
        Map<String, String> response = Map.of(
                ERROR, ExceptionResponse.MICROSERVICE_HAS_PROBLEM.getMessage(),
                MESSAGE, ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}