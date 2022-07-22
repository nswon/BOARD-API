package api.boardAPI.domain.member.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdatePasswordRequestDto {

    private String beforePassword;
    private String afterPassword;
}
