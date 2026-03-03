package org.uteq.resoluciones.resoluciones.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ChangeSetPersister.NotFoundException ex, HttpServletRequest req){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
          ApiError.builder()
                  .timestamp(LocalDateTime.now())
                  .status(404)
                  .error("NOT_FOUND")
                  .message(ex.getMessage())
                  .path(req.getRequestURI())
                  .build()
        );
    }

    @ExceptionHandler(org.apache.coyote.BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest req){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
          ApiError.builder()
                  .timestamp(LocalDateTime.now())
                  .status(400)
                  .error("BAD_REQUEST")
                  .message(ex.getMessage())
                  .path(req.getRequestURI())
                  .build()
        );
    }
}

