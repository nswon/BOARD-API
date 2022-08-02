package api.boardAPI.domain.member.presentation.dto.response;

import api.boardAPI.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberResponseDto {

    private Long id;
    private String email;
    private String nickname;
    private int age;
    private LocalDateTime createdDate;

    @Builder
    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.age = member.getAge();
        this.createdDate = member.getCreatedDate();
    }
}
