package api.boardAPI.domain.member.service;

import api.boardAPI.domain.member.presentation.dto.request.MemberRequestDto;

public interface MemberService {
    Long join(MemberRequestDto requestDto);
}
