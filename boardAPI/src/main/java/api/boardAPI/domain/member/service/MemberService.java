package api.boardAPI.domain.member.service;

import api.boardAPI.domain.member.presentation.dto.request.MemberSignUpRequestDto;
import api.boardAPI.domain.member.presentation.dto.response.MemberResponseDto;

public interface MemberService {
    Long join(MemberSignUpRequestDto requestDto);
    MemberResponseDto findMember(Long id);
}
