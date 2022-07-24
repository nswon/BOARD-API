package api.boardAPI.domain.comment.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentUpdateRequestDto {

    @NotNull(message = "내용을 입력해주세요.")
    @Size(min = 2, max = 4000)
    private String content;

}
