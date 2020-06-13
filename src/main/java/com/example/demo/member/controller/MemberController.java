package com.example.demo.member.controller;

import com.example.demo.member.repository.MemberRepository;
import com.example.demo.member.service.MemberService;
import com.example.demo.member.vo.Member;
import com.example.demo.member.vo.MemberResponseDto;
import com.example.demo.member.vo.MemberSaveRequestDto;
import com.example.demo.overlap.Address;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.catalina.startup.UserConfig;
import org.springframework.boot.context.annotation.UserConfigurations;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

import java.util.Arrays;
import java.util.List;
/**
 * 세션부분 추후 @Aspect 적용하기.
 * */
@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

//     회원 메인 홈
    @RequestMapping("/")
    public String home(){
        log.info("home logger");
        return "home";
    }




    // 회원가입
    @GetMapping("/member/signup")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());

        return "memberAuth/signUp";
    }

    // 회원가입 API
    @PostMapping(value = "/api/member/signup")
    public String create(@Valid MemberForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "memberAuth/signUp";
        }

        Address address = new Address(form.getCity(),
                form.getZipcode(),form.getStreet());
        MemberSaveRequestDto member = new MemberSaveRequestDto();

        memberService.SignUp(member.builder()
                .name(form.getName())
                .address(address)
                .birth(form.getBirth())
                .email(form.getEmail())
                .password(form.getPassword())
                .phone(form.getPhone())
                .build());

        return "memberAuth/signIn";
    }

    //회원정보 리스트
    @GetMapping(value = "/admin/members")
    public String list(Model model) {
        List<MemberResponseDto> members = memberService.findAllDesc();
        model.addAttribute("members", members);

        return "admin/memberList";
    }

    @GetMapping("/member/mypage")
    public String readMyData(Model model, Principal principal, HttpServletRequest request, HttpSession session) {
        Member member = memberRepository.findEmailCheck(principal.getName()); //추후 ASPECT 적용대상
//        Cookie[] myCookies = request.getCookies();
//        log.info("쿠키이름" +myCookies[0].getName());
//        log.info("쿠키값" +myCookies[0].getValue());
//        session.setAttribute(UserCon, userDTO.getId());
//        Object userID = session.getAttribute(UserConfig.)
        if(member != null) {
            model.addAttribute("member", member);
        }

        return "memberAuth/myPage";
    }

    // 회원 정보수정 페이지
    @GetMapping("/member/settings/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        log.info("id : " +id);
        MemberResponseDto dto = memberService.findById(id);
        model.addAttribute("member", dto);
        log.info(dto.getPassword());

        return "memberAuth/settings";
    }

    // 관리자 회원정보 수정페이지
    @GetMapping("/admin/member/settings/{id}")
    public String detailList(@PathVariable Long id, Model model){

        MemberResponseDto dto = memberService.findById(id);

        model.addAttribute("member", dto);
        log.info(dto.getPassword());
        return "admin/settings";
    }

    //로그인 페이지
    @GetMapping("/member/login")
    public String dispLogin() throws Exception
    {
        return "memberAuth/signIn";
    }

    // 회원 로그인 결과
    @GetMapping("/member/login/result")
    public String dispLoginResult()
    {
        return "home";
    }


    // 회원 로그아웃
    @GetMapping("/member/logout/result")
    public String dispLogout()
    {

        return "home";
    }

    // 관리자 정보조회
    @GetMapping("/admin/mypage")
    public String readAdminMyDate(Model model, Principal principal) {

        Member admin = memberRepository.findEmailCheck(principal.getName()); //추후 ASPECT 적용대상E
        if(admin != null) {
            model.addAttribute("admin", admin);
        }
        return "adminAuth/admin_myPage";
    }

    // 관리자 정보수정
    @GetMapping("/admin/settings/{id}")
    public String updateAdminForm(@PathVariable Long id, Model model) {

        MemberResponseDto adminDto = memberService.findById(id);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        model.addAttribute("admin", adminDto);

        return "adminAuth/admin_settings";
    }





}
