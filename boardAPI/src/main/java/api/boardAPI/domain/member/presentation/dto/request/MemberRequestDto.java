package api.boardAPI.domain.member.presentation.dto.request;

import api.boardAPI.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequestDto {

    private String email;
    private String nickname;
    private int age;
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
