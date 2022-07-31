package api.boardAPI;

import api.boardAPI.domain.member.domain.Member;
import api.boardAPI.domain.member.domain.repository.MemberRepository;
import api.boardAPI.domain.member.exception.MemberException;
import api.boardAPI.domain.member.exception.MemberExceptionType;
import api.boardAPI.domain.member.presentation.dto.request.MemberSignUpRequestDto;
import api.boardAPI.domain.member.service.MemberService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class JUnit5_method_Test {

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

    @Test
    public void JUnit5_assertEquals_메서드() throws Exception {
        //given
        MemberSignUpRequestDto requestDto = makeMemberSignUpDto();
        Long memberId = memberService.join(requestDto);
        clear();

        //when
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        //then
        //두 인자값을 비교함
        assertEquals("nam", findMember.getNickname());

        //두 인자의 값을 다르면 메세지 출력
        assertEquals("nam", findMember.getNickname(), "닉네임이 같지 않습니다.");

    }

    @Test
    public void AssertJ_assertThat_메서드() throws Exception {
        //given
        MemberSignUpRequestDto requestDto = makeMemberSignUpDto();
        Long memberId = memberService.join(requestDto);
        clear();

        //when
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        //then
        //두 인자값을 비교함
        assertThat(findMember.getNickname()).isEqualTo("nam");

        //두 인자의 값을 다르면 메세지 출력
        assertThat(findMember.getNickname()).as("닉네임이 같지 않습니다.").isEqualTo("nam");
    }

    @Test
    public void JUnit5_assertAll_and_assertTrue_메서드() throws Exception {
        //given
        MemberSignUpRequestDto requestDto = makeMemberSignUpDto();
        Long memberId = memberService.join(requestDto);
        clear();

        //when
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        String nickname = findMember.getNickname();

        //then
        //여러개의 assertions 가 만족해야지 테스트를 통과함
        assertAll("nickname",
                () -> assertTrue(nickname.startsWith("n")),
                () -> assertTrue(nickname.endsWith("m")));
    }

    @Test
    public void AssertJ_isTrue_메서드() throws Exception {
        //given
        MemberSignUpRequestDto requestDto = makeMemberSignUpDto();
        Long memberId = memberService.join(requestDto);
        clear();

        //when
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        String nickname = findMember.getNickname();
        boolean result = nickname.contains("n");

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void JUnit5_assertThrows_메서드() throws Exception {
        //given
        MemberSignUpRequestDto requestDto = MemberSignUpRequestDto.builder()
                .email(null)
                .nickname("nam")
                .age(18)
                .password("nam1234@")
                .build();

        //when

        //then
        //특정 예외가 발생하였는지 확인하고 싶을 때 사용
        assertThrows(Exception.class, () -> memberService.join(requestDto));
    }

    @Test
    public void AssertJ_assertThatThrownBy_메서드() throws Exception {
        //given
        MemberSignUpRequestDto requestDto = MemberSignUpRequestDto.builder()
                .email(null)
                .nickname("nam")
                .age(18)
                .password("nam1234@")
                .build();

        //when

        //then
        assertThatThrownBy(() -> memberService.join(requestDto))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("null");
    }
}
