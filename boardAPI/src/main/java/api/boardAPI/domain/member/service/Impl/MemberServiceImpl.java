package api.boardAPI.domain.member.service.Impl;

import api.boardAPI.domain.member.domain.Member;
import api.boardAPI.domain.member.domain.repository.MemberRepository;
import api.boardAPI.domain.member.presentation.dto.request.MemberSignUpRequestDto;
import api.boardAPI.domain.member.presentation.dto.request.MemberUpdateRequestDto;
import api.boardAPI.domain.member.presentation.dto.response.MemberResponseDto;
import api.boardAPI.domain.member.service.MemberService;
import api.boardAPI.global.security.jwt.JwtTokenProvider;
import api.boardAPI.global.security.jwt.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        Member member = memberRepository.save(requestDto.toEntity());
        member.addUserAuthority();
        member.encodePassword(passwordEncoder);
        log.info("회원가입할 때 입력한 이메일 : " + member.getUsername());
        return member.getId();
    }

    @Transactional
    @Override
    public String login(Map<String, String> members) {
        Member member = memberRepository.findByEmail(members.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 Email 입니다."));

        if (!passwordEncoder.matches(members.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        String role = member.getRole().name();
        String token = jwtTokenProvider.createToken(member.getUsername(), role);
        log.info("발급한 토큰 값 : " + token);
        return token;
    }

    @Override
    public MemberResponseDto findMember(Long id) {
        return memberRepository.findById(id)
                .map(MemberResponseDto::new)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    }

    @Override
    public MemberResponseDto findMyInfo() {
        return memberRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .map(MemberResponseDto::new)
                .orElseThrow(() -> new IllegalArgumentException("로그인 후 이용해주세요."));
    }

    @Transactional
    @Override
    public Long updateMember(MemberUpdateRequestDto requestDto) {
        Member member = memberRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new IllegalArgumentException("로그인 후 이용해주세요."));

        member.update(requestDto.getNickname(), requestDto.getAge());
        return member.getId();
    }

    @Transactional
    @Override
    public Long updatePassword(String beforePassword, String afterPassword) {
        Member member = memberRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new IllegalArgumentException("로그인 후 이용해주세요."));

        if(!member.matchPassword(passwordEncoder, beforePassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        member.updatePassword(passwordEncoder, afterPassword);
        return member.getId();
    }

    @Transactional
    @Override
    public Long Withdrawal(String checkPassword) {
        Member member = memberRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new IllegalArgumentException("로그인 후 이용해주세요."));

        if(!member.matchPassword(passwordEncoder, checkPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        memberRepository.delete(member);
        return member.getId();
    }

}
