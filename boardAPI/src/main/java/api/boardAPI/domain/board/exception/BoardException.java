package api.boardAPI.domain.board.exception;

import api.boardAPI.global.exception.BaseException;
import api.boardAPI.global.exception.BaseExceptionType;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
public class BoardException extends BaseException {

    private BaseExceptionType baseExceptionType;

    @Override
    public BaseExceptionType getExceptionType() {
        return baseExceptionType;
    }
}
