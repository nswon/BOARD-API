package api.boardAPI.global.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity handleBaseException(BaseException exception) {
        log.error("BaseException errorMessage(): {}", exception.getExceptionType().getErrorMessage());
        log.error("BaseException errorCode(): {}", exception.getExceptionType().getErrorCode());

        return new ResponseEntity(new ExceptionDto(exception.getExceptionType().getErrorCode(), exception.getExceptionType().getErrorMessage()), exception.getExceptionType().getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity hadleMemberException(Exception exception) {
        exception.printStackTrace();
        return new ResponseEntity(HttpStatus.OK);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        ExceptionDto dto = ExceptionDto.builder()
                .errorCode(405)
                .errorMessage("잘못된 메서드 요청입니다.").build();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(dto);
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity handleUnauthorized(HttpClientErrorException.Unauthorized e) {
        ExceptionDto dto = ExceptionDto.builder()
                .errorCode(401)
                .errorMessage("토큰이 유효하지 않습니다.").build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity handleForbidden(HttpClientErrorException.Forbidden e) {
        ExceptionDto dto = ExceptionDto.builder()
                .errorCode(403)
                .errorMessage("권한이 없습니다.").build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String processValidationError(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append("\n 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("] \n");
        }

        return builder.toString();
    }

    @Data
    @AllArgsConstructor
    @Builder
    static class ExceptionDto {
        private Integer errorCode;
        private String errorMessage;
    }
}
