package org.example.testtasknerdy.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.testtasknerdy.model.Member;
import org.example.testtasknerdy.repository.MemberRepository;
import org.example.testtasknerdy.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    @Override
    public Member addMember(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public void deleteMember(Long id) {
        memberRepository.delete(findById(id));
    }

    @Override
    public Member updateMember(Long id, Member member) {
        if(findById(id) != null) member.setId(id);
        return memberRepository.save(member);
    }
}
