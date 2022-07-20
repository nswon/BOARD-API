package api.boardAPI.domain.member.service.Impl;

import api.boardAPI.domain.member.domain.Member;
import api.boardAPI.domain.member.domain.repository.MemberRepository;
import api.boardAPI.domain.member.presentation.dto.request.MemberSignUpRequestDto;
import api.boardAPI.domain.member.presentation.dto.response.MemberResponseDto;
import api.boardAPI.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Long join(MemberSignUpRequestDto requestDto) {
        if(memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Member member = memberRepository.save(requestDto.toEntity());
        member.addUserAuthority();
        return member.getId();
    }

    @Override
    public MemberResponseDto findMember(Long id) {
        return memberRepository.findById(id)
                .map(MemberResponseDto::new)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용하자 존재하지 않습니다."));
    }
}
