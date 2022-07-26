package api.boardAPI.domain.member.service;

import api.boardAPI.domain.member.presentation.dto.request.*;
import api.boardAPI.domain.member.presentation.dto.response.MemberResponseDto;
import api.boardAPI.domain.member.presentation.dto.response.TokenResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MemberService {
    Long join(MemberSignUpRequestDto requestDto);

    TokenResponseDto login(MemberSignInRequestDto requestDto);

    TokenResponseDto issueAccessToken(HttpServletRequest request);
    MemberResponseDto findMember(Long id);

    MemberResponseDto findMyInfo();

    Long updateMember(MemberUpdateRequestDto requestDto);

    Long updatePassword(String beforePassword, String afterPassword);

    Long withdrawal(String checkPassword);

    Long addAdminAuthority(MemberAdminRequestDto requestDto);

    List<MemberResponseDto> allMemberList();
    Long withdrawalMember(Long memberId, String password);
}
