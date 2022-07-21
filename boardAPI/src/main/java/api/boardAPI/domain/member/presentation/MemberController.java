package api.boardAPI.domain.member.presentation;

import api.boardAPI.domain.member.presentation.dto.request.MemberRequestDto;
import api.boardAPI.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public Long join(@RequestBody MemberRequestDto requestDto) {
        return memberService.join(requestDto);
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> member) {
        return memberService.login(member);
    }

    @GetMapping("/test")
    public String test() {
        return "WOW! DONE!!";
    }
}
