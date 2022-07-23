package api.boardAPI.global.exception;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {
    int getErrorCode(); //에러코드

    HttpStatus getHttpStatus(); //HTTP 상태

    String getErrorMessage(); //에러메세지
}
