package api.boardAPI.domain.member.presentation;

import api.boardAPI.domain.member.presentation.dto.request.MemberSignUpRequestDto;
import api.boardAPI.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String signup() {
        return "/member/signup.html";
    }

    @PostMapping("/join")
    public String join(MemberSignUpRequestDto requestDto) {
        memberService.join(requestDto);
        return "redirect:/";
    }

    @GetMapping("/find/{id}")
    public String findMember(@PathVariable("id") Long id, Model model) {
        model.addAttribute("findMember", memberService.findMember(id));
        return "/member/profile.html";
    }
}
