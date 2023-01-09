package com.team1.spreet.exception.handler;

import com.team1.spreet.dto.ErrorResponseDto;
import com.team1.spreet.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RestApiExceptionHandler {

    // RestApiException 에러 핸들링
    @ExceptionHandler(RestApiException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleCustomException(RestApiException e) {
        return new ErrorResponseDto(e.getMsg(), e.getStatusCode());
    }

    // MethodArgumentNotValid 에러 핸들링
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        FieldError fieldError = bindingResult.getFieldError();

            sb.append("[");
            sb.append(fieldError.getField());
            sb.append("](은)는");
            sb.append(fieldError.getDefaultMessage());
            sb.append(" 입력된 값: [");
            sb.append(fieldError.getRejectedValue());
            sb.append("]");

        return new ErrorResponseDto(sb.toString(), HttpStatus.BAD_REQUEST.value());
    }
}