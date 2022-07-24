package api.boardAPI.domain.board.exception;

import api.boardAPI.global.exception.BaseExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BoardExceptionType implements BaseExceptionType {

    NOT_FOUND_BOARD(605, HttpStatus.OK, "존재하지 않는 게시글입니다."),
    DIFFERENT_MEMBER_NOT_UPDATE(606, HttpStatus.OK, "다른 회원 게시글은 수정할 수 없습니다."),
    DIFFERENT_MEMBER_NOT_DELETE(607, HttpStatus.OK, "다른 회원 게시글은 삭제할 수 없습니다.");

    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
