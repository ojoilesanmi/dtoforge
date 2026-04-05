package io.github.ojoilesanmi.dtoforge.web.advice;

import io.github.ojoilesanmi.dtoforge.shared.ErrorCodes;
import io.github.ojoilesanmi.dtoforge.shared.exception.InvalidJsonException;
import io.github.ojoilesanmi.dtoforge.shared.exception.InvalidJsonStructureException;
import io.github.ojoilesanmi.dtoforge.shared.exception.InvalidOutputStyleException;
import io.github.ojoilesanmi.dtoforge.shared.exception.UnsupportedGenerationStyleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidJsonException.class)
    public ProblemDetail handleInvalidJson(InvalidJsonException ex) {
        return forBadRequest("Invalid JSON", ErrorCodes.INVALID_JSON, ex.getMessage());
    }

    @ExceptionHandler(InvalidJsonStructureException.class)
    public ProblemDetail handleInvalidStructure(InvalidJsonStructureException ex) {
        return forBadRequest("Invalid JSON Structure", ErrorCodes.INVALID_STRUCTURE, ex.getMessage());
    }

    @ExceptionHandler(InvalidOutputStyleException.class)
    public ProblemDetail handleInvalidOutputStyle(InvalidOutputStyleException ex) {
        return forBadRequest("Invalid Output Style", ErrorCodes.INVALID_OUTPUT_STYLE, ex.getMessage());
    }

    @ExceptionHandler(UnsupportedGenerationStyleException.class)
    public ProblemDetail handleUnsupportedStyle(UnsupportedGenerationStyleException ex) {
        return forBadRequest("Unsupported Generation Style", ErrorCodes.UNSUPPORTED_STYLE, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problem.setTitle("Validation Error");
        problem.setProperty("errorCode", ErrorCodes.VALIDATION_ERROR);
        problem.setProperty("fieldErrors", ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList());
        return problem;
    }

    private ProblemDetail forBadRequest(String title, String errorCode, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        problem.setTitle(title);
        problem.setProperty("errorCode", errorCode);
        return problem;
    }
}
