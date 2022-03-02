package com.kjwon.template.controller;

import com.kjwon.template.model.MemberInput;
import com.kjwon.template.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class MainController {

    private  static final Logger log = Logger.getLogger(MainController.class);

    private final MemberService memberService;

    @GetMapping("/")
    public String index(){

        return "index";
    }

    @GetMapping("/member/login")
    public String login(){

        return "member/login";
    }

    @GetMapping("/member/register")
    public String register(){

        return "member/register";
    }

    @PostMapping("/member/register")
    public String registerSubmit(MemberInput parameter){
        System.out.println("test");

        System.out.println(parameter.getUserName());

        boolean result = memberService.register(parameter);

        return "index";
    }

    @GetMapping("/member/userInfo")
    public String userInfo(){

        return "member/userInfo";
    }
}
