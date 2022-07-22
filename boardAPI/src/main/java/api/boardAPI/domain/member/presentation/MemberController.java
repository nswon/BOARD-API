package api.boardAPI.domain.member.presentation;

import api.boardAPI.domain.member.presentation.dto.request.MemberDeleteRequestDto;
import api.boardAPI.domain.member.presentation.dto.request.MemberSignUpRequestDto;
import api.boardAPI.domain.member.presentation.dto.request.MemberUpdatePasswordRequestDto;
import api.boardAPI.domain.member.presentation.dto.request.MemberUpdateRequestDto;
import api.boardAPI.domain.member.presentation.dto.response.MemberResponseDto;
import api.boardAPI.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     */
    @PostMapping("/join")
    public Long join(@RequestBody MemberSignUpRequestDto requestDto) {
        return memberService.join(requestDto);
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> member) {
        return memberService.login(member);
    }

    /**
     * 다른 회원정보 조회
     */
    @GetMapping("/find/{id}")
    public MemberResponseDto findMember(@PathVariable("id") Long id) {
        return memberService.findMember(id);
    }

    /**
     * 내정보 조회
     */
    @GetMapping("/myInfo")
    public MemberResponseDto findMyInfo() {
        return memberService.findMyInfo();
    }

    /**
     * 내정보 수정
     */
    @PutMapping("/myInfo/edit")
    public Long updateMember(@RequestBody MemberUpdateRequestDto requestDto) {
        return memberService.updateMember(requestDto);
    }

    /**
     * 비밀번호 수정
     */
    @PutMapping("/myInfo/password")
    public Long updatePassword(@RequestBody MemberUpdatePasswordRequestDto requestDto) {
        return memberService.updatePassword(requestDto.getBeforePassword(), requestDto.getAfterPassword());
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping("/delete")
    public Long withdrawal(@RequestBody MemberDeleteRequestDto requestDto) {
        return memberService.Withdrawal(requestDto.getPassword());
    }
}
