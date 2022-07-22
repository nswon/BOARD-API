package api.boardAPI.domain.member.presentation;

import api.boardAPI.domain.member.presentation.dto.request.MemberRequestDto;
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

    @PostMapping("/join")
    public Long join(@RequestBody MemberRequestDto requestDto) {
        return memberService.join(requestDto);
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> member) {
        return memberService.login(member);
    }
}
