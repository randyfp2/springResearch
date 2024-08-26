package com.research.randy.exception;

import com.research.randy.apiResponse.apiResponse;
import com.research.randy.apiResponse.util.apiResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.sql.SQLException;

public class globalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<apiResponse<String>> handleAllExceptions(Exception ex, WebRequest request) {
        apiResponse<String> apiResponse = apiResponseUtil.createErrorResponse("Internal Server Error: " + ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<apiResponse<String>> handleSQLException(SQLException ex, WebRequest request) {
        apiResponse<String> apiResponse = apiResponseUtil.createErrorResponse("Database Error: " + ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Tambahkan metode untuk exception lain yang ingin Anda tangani
}
