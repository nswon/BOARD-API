package api.boardAPI.domain.member.service.Impl;

import api.boardAPI.domain.member.domain.Member;
import api.boardAPI.domain.member.domain.repository.MemberRepository;
import api.boardAPI.domain.member.presentation.dto.request.MemberRequestDto;
import api.boardAPI.domain.member.service.MemberService;
import api.boardAPI.global.security.jwt.JwtTokenProvider;
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
    public Long join(MemberRequestDto requestDto) {
        if (memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        Member member = memberRepository.save(requestDto.toEntity());
        member.addUserAuthority();
        member.encodePassword(passwordEncoder);
        log.info("회원가입할 때 입력한 이메일 : " + member.getUsername());
        return member.getId();
    }

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
}
