package com.edu.ulab.app.web.handler;

import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.web.response.BaseWebResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BaseWebResponse> handleNotFoundExceptionException(@NonNull final NotFoundException exc) {
        log.error(exc.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new BaseWebResponse(createErrorMessage(exc)));
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseWebResponse> handleIllegalArgumentException(@NonNull final IllegalArgumentException exc) {
    	log.error(exc.getMessage());
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    			.body(new BaseWebResponse(createErrorMessage(exc)));
    }
    
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<BaseWebResponse> handleNoSuchElementException(@NonNull final NoSuchElementException exc) {
    	log.error(exc.getMessage());
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    			.body(new BaseWebResponse(createErrorMessage(exc)));
    }
    
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<BaseWebResponse> handleDataAccessAndSQLException (@NonNull final DataAccessException exc) {
    	log.error(exc.getMessage());
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    			.body(new BaseWebResponse(createErrorMessage(exc)));
    }
    
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<BaseWebResponse> handleSQLException (@NonNull final SQLException exc) {
    	log.error(exc.getMessage());
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    			.body(new BaseWebResponse(createErrorMessage(exc)));
    }

    private String createErrorMessage(Exception exception) {
        final String message = exception.getMessage();
        log.error(ExceptionHandlerUtils.buildErrorMessage(exception));
        return message;
    }
}
