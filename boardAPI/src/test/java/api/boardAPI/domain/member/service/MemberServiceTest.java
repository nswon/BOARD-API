package api.boardAPI.domain.member.service;

import api.boardAPI.domain.member.domain.Member;
import api.boardAPI.domain.member.domain.repository.MemberRepository;
import api.boardAPI.domain.member.exception.MemberException;
import api.boardAPI.domain.member.exception.MemberExceptionType;
import api.boardAPI.domain.member.presentation.dto.request.MemberSignUpRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest //스프링 컨테이너안에서 테스트를 돌리기 위함
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

    @Test
    public void 회원가입_성공() {
        //given
        MemberSignUpRequestDto requestDto = MemberSignUpRequestDto.builder()
                .email("nam@gmail.com")
                .nickname("nam")
                .age(18)
                .password("nam1234@")
                .build();

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
        MemberSignUpRequestDto requestDto = MemberSignUpRequestDto.builder().email("test@gmail.com").nickname("test").age(18).password("test1234@").build();
        MemberSignUpRequestDto requestDto2 = MemberSignUpRequestDto.builder().email("test@gmail.com").nickname("test").age(18).password("test1234@").build();

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
        Assertions.assertThrows(Exception.class, ()-> memberService.join(requestDto));
        Assertions.assertThrows(Exception.class, ()-> memberService.join(requestDto2));
        Assertions.assertThrows(Exception.class, ()-> memberService.join(requestDto3));
        Assertions.assertThrows(Exception.class, ()-> memberService.join(requestDto4));
    }


}