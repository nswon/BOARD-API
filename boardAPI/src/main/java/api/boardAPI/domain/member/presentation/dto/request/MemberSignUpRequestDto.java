package api.boardAPI.domain.member.presentation.dto.request;

import api.boardAPI.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSignUpRequestDto {

    @NotNull(message = "이메일을 입력해주세요.")
    @Email
    private String email;

    @NotNull(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 20)
    private String nickname;

    @NotNull(message = "나이를 입력해주세요.")
    @Positive //양수만 허용
    private int age;

    @NotNull(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{6,20}",
            message = "영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 6자 ~ 20자여야 합니다.")
    private String password;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .age(age)
                .password(password)
                .build();
    }

}
