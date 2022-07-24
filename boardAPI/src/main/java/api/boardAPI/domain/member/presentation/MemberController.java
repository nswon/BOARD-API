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

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public Long join(@RequestBody @Valid MemberSignUpRequestDto requestDto) {
        return memberService.join(requestDto);
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid Map<String, String> member) {
        return memberService.login(member);
    }

    @GetMapping("/find/{id}")
    public MemberResponseDto findMember(@PathVariable("id") Long id) {
        return memberService.findMember(id);
    }

    @GetMapping("/myInfo")
    public MemberResponseDto findMyInfo() {
        return memberService.findMyInfo();
    }

    @PutMapping("/myInfo/edit")
    public Long updateMember(@RequestBody @Valid MemberUpdateRequestDto requestDto) {
        return memberService.updateMember(requestDto);
    }

    @PutMapping("/myInfo/password")
    public Long updatePassword(@RequestBody @Valid MemberUpdatePasswordRequestDto requestDto) {
        return memberService.updatePassword(requestDto.getBeforePassword(), requestDto.getAfterPassword());
    }

    @DeleteMapping("/delete")
    public Long withdrawal(@RequestBody @Valid MemberDeleteRequestDto requestDto) {
        return memberService.Withdrawal(requestDto.getPassword());
    }

    @GetMapping("/admin")
    public String admin() {
        return "hi admin";
    }
}
