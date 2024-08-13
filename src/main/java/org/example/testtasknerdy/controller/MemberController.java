package org.example.testtasknerdy.controller;

import lombok.RequiredArgsConstructor;
import org.example.testtasknerdy.model.Member;
import org.example.testtasknerdy.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public List<Member> getMembers() {
        return memberService.findAll();
    }

    @GetMapping("/{id}")
    public Member getMember(@PathVariable long id) {
        return memberService.findById(id);
    }

    @PostMapping
    public Member createMember(@RequestBody Member member) {
        return memberService.addMember(member);
    }

    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable long id) {
        memberService.deleteMember(id);
    }

    @PutMapping("/{id}")
    public Member updateMember(@PathVariable long id, @RequestBody Member member) {
        return memberService.updateMember(id, member);
    }



}
