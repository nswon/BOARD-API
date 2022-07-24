package api.boardAPI.domain.comment.exception;

import api.boardAPI.global.exception.BaseExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentExceptionType implements BaseExceptionType {

    NOT_FOUND_COMMENT(606, HttpStatus.OK, "존재하지 않는 댓글입니다."),
    DIFFERENT_MEMBER_NOT_UPDATE(607, HttpStatus.OK, "다른 회원 댓글은 수정할 수 없습니다."),
    DIFFERENT_MEMBER_NOT_DELETE(608, HttpStatus.OK, "다른 회원 댓글은 삭제할 수 없습니다.");

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
