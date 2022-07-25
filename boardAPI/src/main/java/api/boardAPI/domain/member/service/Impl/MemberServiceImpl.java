package api.boardAPI.domain.member.service.Impl;

import api.boardAPI.domain.member.domain.Member;
import api.boardAPI.domain.member.domain.repository.MemberRepository;
import api.boardAPI.domain.member.exception.MemberException;
import api.boardAPI.domain.member.exception.MemberExceptionType;
import api.boardAPI.domain.member.presentation.dto.request.*;
import api.boardAPI.domain.member.presentation.dto.response.MemberResponseDto;
import api.boardAPI.domain.member.service.MemberService;
import api.boardAPI.global.security.jwt.JwtTokenProvider;
import api.boardAPI.global.security.jwt.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public Long join(MemberSignUpRequestDto requestDto) {
        if (memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new MemberException(MemberExceptionType.ALREADY_EXIST_EMAIL);
        }

        Member member = memberRepository.save(requestDto.toEntity());
        member.addUserAuthority();
        member.encodePassword(passwordEncoder);
        return member.getId();
    }

    @Transactional
    @Override
    public String login(MemberSignInRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_SIGNUP_EMAIL));

        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
        }

        String role = member.getRole().name();
        return jwtTokenProvider.createToken(member.getUsername(), role);
    }

    @Override
    public MemberResponseDto findMember(Long id) {
        return memberRepository.findById(id)
                .map(MemberResponseDto::new)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
    }

    @Override
    public MemberResponseDto findMyInfo() {
        Member member = validateLoginStatus();
        return MemberResponseDto.builder()
                .member(member)
                .build();
    }

    @Transactional
    @Override
    public Long updateMember(MemberUpdateRequestDto requestDto) {
        Member member = validateLoginStatus();
        member.update(requestDto.getNickname(), requestDto.getAge());
        return member.getId();
    }

    @Transactional
    @Override
    public Long updatePassword(String beforePassword, String afterPassword) {
        Member member = validateLoginStatus();
        if (!member.matchPassword(passwordEncoder, beforePassword)) {
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
        }

        member.updatePassword(passwordEncoder, afterPassword);
        return member.getId();
    }

    @Transactional
    @Override
    public Long withdrawal(String checkPassword) {
        Member member = validateLoginStatus();
        if (!member.matchPassword(passwordEncoder, checkPassword)) {
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
        }

        memberRepository.delete(member);
        return member.getId();
    }

    @Transactional
    @Override
    public Long addAdminAuthority(MemberAdminRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_SIGNUP_EMAIL));

        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
        }

        member.addAdminAuthority();
        return member.getId();
    }

    @Override
    public List<MemberResponseDto> allMemberList() {
        return memberRepository.findAll().stream()
                .map(MemberResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Long withdrawalMember(Long memberId, String password) {
        Member member = validateLoginStatus();
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
        }

        memberRepository.delete(findMember);
        return member.getId();
    }

    private Member validateLoginStatus() {
        return memberRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new MemberException(MemberExceptionType.REQUIRED_DO_LOGIN));
    }
}
