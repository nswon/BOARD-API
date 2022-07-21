package api.boardAPI.domain.member.service;

import api.boardAPI.domain.member.presentation.dto.request.MemberRequestDto;

import java.util.Map;

public interface MemberService {
    Long join(MemberRequestDto requestDto);
    String login(Map<String, String> members);
}
