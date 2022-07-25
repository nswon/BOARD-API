package api.boardAPI.domain.member.presentation;

import api.boardAPI.domain.member.presentation.dto.request.*;
import api.boardAPI.domain.member.presentation.dto.response.MemberResponseDto;
import api.boardAPI.domain.member.service.MemberService;
import api.boardAPI.global.wrap.Wrap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

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
    public String login(@RequestBody @Valid MemberSignInRequestDto requestDto) {
        return memberService.login(requestDto);
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
        return memberService.withdrawal(requestDto.getPassword());
    }

    @PostMapping("/admin")
    public Long addAdminAuthority(@RequestBody @Valid MemberAdminRequestDto requestDto) {
        return memberService.addAdminAuthority(requestDto);
    }

    @GetMapping("/admin/findAll")
    public Wrap findAllMemberList() {
        return new Wrap(memberService.allMemberList());
    }

    @DeleteMapping("/admin/delete/{memberId}")
    public Long withdrawalMember(@PathVariable("memberId") Long memberId,
                                 @RequestBody @Valid MemberDeleteRequestDto requestDto) {
        return memberService.withdrawalMember(memberId, requestDto.getPassword());
    }
}
