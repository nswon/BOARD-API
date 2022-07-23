package api.boardAPI.domain.member.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateRequestDto {

    @NotNull(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 20)
    private String nickname;

    @NotNull(message = "나이를 입력해주세요.")
    @Positive //양수만 허용
    private int age;
}
