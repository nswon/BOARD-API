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

    /**
     * 입력한 이메일이 이미 가입된 이메일은 아닌지 확인합니다. (이메일은 unique 해야 합니다)
     * 가입할 수 있는 이메일이라면 저장한 후 USER 권한을 부여합니다.
     * 비밀번호를 암호화시킵니다.
     */
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

    /**
     * 입력한 이메일과 비밀번호가 일치하는지 확인합니다.
     * 맞다면 Access Token, Refresh Token 을 반환합니다.
     */
    @Transactional
    @Override
    public String login(MemberSignInRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_SIGNUP_EMAIL));
        validateMatchedPassword(requestDto.getPassword(), member.getPassword());

        return jwtTokenProvider.createAccessToken(member.getUsername(), member.getRole().name());
    }

    /**
     * 다른 회원을 찾습니다.
     */
    @Override
    public MemberResponseDto findMember(Long id) {
        return memberRepository.findById(id)
                .map(MemberResponseDto::new)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
    }

    /**
     * 먼저 로그인을 했는지 확인합니다.
     * 아이디와 이메일, 닉네임, 나이를 반환합니다.
     */
    @Override
    public MemberResponseDto findMyInfo() {
        Member member = validateLoginStatus();
        return MemberResponseDto.builder()
                .member(member)
                .build();
    }

    /**
     * 먼저 로그인을 했는지 확인합니다.
     * 이메일은 수정할 수 없습니다. 비밀번호 수정은 따로 빼두었습니다.
     */
    @Transactional
    @Override
    public Long updateMember(MemberUpdateRequestDto requestDto) {
        Member member = validateLoginStatus();
        member.update(requestDto.getNickname(), requestDto.getAge());
        return member.getId();
    }

    /**
     * 먼저 로그인을 했는지 확인합니다.
     * 입력한 기존의 비밀번호가 일치하는지 확인합니다.
     */
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

    /**
     * 먼저 로그인을 했는지 확인합니다.
     * 로그인을 했다면 입력한 비밀번호가 맞는지 체크합니다. 맞다면 해당 회원을 삭제합니다.
     */
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

    /**
     * 입력한 이메일과 비밀번호가 맞는지 체크합니다. 맞다면 ADMIN 권한을 부여합니다.
     */
    @Transactional
    @Override
    public Long addAdminAuthority(MemberAdminRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_SIGNUP_EMAIL));
        validateMatchedPassword(requestDto.getPassword(), member.getPassword());

        member.addAdminAuthority();
        return member.getId();
    }

    /**
     * 모든 회원들을 조회합니다.
     */
    @Override
    public List<MemberResponseDto> allMemberList() {
        return memberRepository.findAll().stream()
                .map(MemberResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 먼저 로그인을 했는지 확인합니다.
     * 삭제할 회원을 찾습니다. 만약 회원이 존재하지 않다면 예외를 던집니다.
     * 그리고 입력한 비밀번호가 일치하는지 확인합니다.
     * 일치한다면 해당 회원을 삭제합니다.
     */
    @Transactional
    @Override
    public Long withdrawalMember(Long memberId, String password) {
        Member member = validateLoginStatus();
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        validateMatchedPassword(password, member.getPassword());

        memberRepository.delete(findMember);
        return member.getId();
    }

    private Member validateLoginStatus() {
        return memberRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new MemberException(MemberExceptionType.REQUIRED_DO_LOGIN));
    }

    private void validateMatchedPassword(String validPassword, String memberPassword) {
        if (!passwordEncoder.matches(validPassword, memberPassword)) {
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
        }
    }
}
