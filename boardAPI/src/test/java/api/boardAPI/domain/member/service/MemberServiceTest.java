package api.boardAPI.domain.member.service;

import api.boardAPI.domain.member.domain.Member;
import api.boardAPI.domain.member.domain.Role;
import api.boardAPI.domain.member.domain.repository.MemberRepository;
import api.boardAPI.domain.member.exception.MemberException;
import api.boardAPI.domain.member.exception.MemberExceptionType;
import api.boardAPI.domain.member.presentation.dto.request.MemberSignUpRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest //스프링부트에서 테스트를 하려면 꼭 붙여야 함
@Transactional //테스트를 실행하고 끝이나면 롤백함
class MemberServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    private void clear() {
        em.flush();
        em.clear();
    }

    private MemberSignUpRequestDto makeMemberSignUpDto() {
        return MemberSignUpRequestDto.builder()
                .email("nam@gmail.com")
                .nickname("nam")
                .age(18)
                .password("nam1234@")
                .build();
    }

    private MemberSignUpRequestDto setMember() {
//        회원가입 요청
        MemberSignUpRequestDto requestDto = MemberSignUpRequestDto.builder().email("nam@gmail.com").nickname("nam").age(18).password("nam1234@").build();

//        회원가입
        memberService.join(requestDto);
        clear();

//        빈 컨텍스트 생성
        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();

//        빈 컨텍스트에다가 회원가입한 아이디(이메일)과 비번, 권한이름을 넣어줌
        emptyContext.setAuthentication(new UsernamePasswordAuthenticationToken(Member.builder()
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .role(Role.ROLE_USER)
                .build(),
                null, null));

//        컨텍스트에다 저장
        SecurityContextHolder.setContext(emptyContext);
        return requestDto;
    }

    @Test
    public void 회원가입_성공() {
        //given
        MemberSignUpRequestDto requestDto = makeMemberSignUpDto();

        //when
        memberService.join(requestDto);
        clear();

        //then
        Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        assertThat(member.getId()).isNotNull();
        assertThat(member.getEmail()).isEqualTo(requestDto.getEmail());
        assertThat(member.getNickname()).isEqualTo(requestDto.getNickname());
        assertThat(member.getAge()).isEqualTo(requestDto.getAge());
        assertThat(member.getRole().name()).isEqualTo("ROLE_USER");
    }

    @Test
    public void 회원가입_실패_이메일중복() {
        //given
        MemberSignUpRequestDto requestDto = makeMemberSignUpDto();
        MemberSignUpRequestDto requestDto2 = makeMemberSignUpDto();

        //when
        memberService.join(requestDto);
        clear();
        try {
            memberService.join(requestDto2);
            clear();
        } catch (MemberException e) {
            return;
        }

        //then
        fail("예외가 발생해야 한다.");
    }

    @Test
    public void 회원가입_실패_입력하지않은필드() throws Exception {
        //given
        MemberSignUpRequestDto requestDto = MemberSignUpRequestDto.builder().email(null).nickname("test").age(18).password("test1234@").build();
        MemberSignUpRequestDto requestDto2 = MemberSignUpRequestDto.builder().email("test2@gmail.com").nickname(null).age(18).password("test1234@").build();
        MemberSignUpRequestDto requestDto3 = MemberSignUpRequestDto.builder().email("test3@gmail.com").nickname("test3").age(0).password("test1234@").build();
        MemberSignUpRequestDto requestDto4 = MemberSignUpRequestDto.builder().email("test4@gmail.com").nickname("test4").age(18).password(null).build();

        //when

        //then
        assertThrows(Exception.class, ()-> memberService.join(requestDto));
        assertThrows(Exception.class, ()-> memberService.join(requestDto2));
        assertThrows(Exception.class, ()-> memberService.join(requestDto3));
        assertThrows(Exception.class, ()-> memberService.join(requestDto4));
    }

    //TODO : 로그인 성공, 실패 테스트 작성 전 토큰 유효성 테스트 짜기
}