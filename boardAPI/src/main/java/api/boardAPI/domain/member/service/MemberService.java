package api.boardAPI.domain.member.service;

import api.boardAPI.domain.member.presentation.dto.request.MemberSignInRequestDto;
import api.boardAPI.domain.member.presentation.dto.request.MemberSignUpRequestDto;
import api.boardAPI.domain.member.presentation.dto.request.MemberUpdateRequestDto;
import api.boardAPI.domain.member.presentation.dto.response.MemberResponseDto;

import java.util.Map;

public interface MemberService {
    Long join(MemberSignUpRequestDto requestDto);

    String login(MemberSignInRequestDto requestDto);

    MemberResponseDto findMember(Long id);

    MemberResponseDto findMyInfo();

    Long updateMember(MemberUpdateRequestDto requestDto);

    Long updatePassword(String beforePassword, String afterPassword);

    Long Withdrawal(String checkPassword);
}
