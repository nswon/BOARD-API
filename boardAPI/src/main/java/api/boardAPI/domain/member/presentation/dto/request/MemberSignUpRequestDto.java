package api.boardAPI.domain.member.presentation.dto.request;

import api.boardAPI.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberSignUpRequestDto {

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

    @Builder
    public MemberSignUpRequestDto(String email, String nickname, int age, String password) {
        this.email = email;
        this.nickname = nickname;
        this.age = age;
        this.password = password;
    }
}
