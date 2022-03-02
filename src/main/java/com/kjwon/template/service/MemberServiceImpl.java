package com.kjwon.template.service;

//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.kjwon.template.entity.Member;
import com.kjwon.template.model.MemberInput;
import com.kjwon.template.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findById(username);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();
/*
        if (Member.MEMBER_STATUS_REQ.equals(member.getUserStatus())) {
            throw new MemberNotEmailAuthException("이메일 활성화 이후에 로그인을 해주세요.");
        }

        if (Member.MEMBER_STATUS_STOP.equals(member.getUserStatus())) {
            throw new MemberStopUserException("정지된 회원 입니다.");
        }

        if (Member.MEMBER_STATUS_WITHDRAW.equals(member.getUserStatus())) {
            throw new MemberStopUserException("탈퇴된 회원 입니다.");
        }
        */
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
/*
        if (member.isAdminYn()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }*/

        return new User(member.getUserId(), member.getPassword(), grantedAuthorities);
//        return null;
    }

    public boolean register(MemberInput parameter) {

        Optional<Member> optionalMember = memberRepository.findById(parameter.getUserId());
        if (optionalMember.isPresent()) {
            //현재 userId에 해당하는 데이터 존재
            return false;
        }

        String encPassword = BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt());
        String uuid = UUID.randomUUID().toString();

        Member member = Member.builder()
                .userId(parameter.getUserId())
                .userName(parameter.getUserName())
                .phone(parameter.getPhone())
                .password(encPassword)
                .regDt(LocalDateTime.now())
                .build();
        memberRepository.save(member);

        return true;
    }
}
