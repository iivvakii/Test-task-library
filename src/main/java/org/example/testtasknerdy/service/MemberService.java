package org.example.testtasknerdy.service;

import org.example.testtasknerdy.model.Member;

import java.util.List;

public interface MemberService {
    List<Member> findAll();
    Member findById(Long id);
    Member addMember(Member member);
    void deleteMember(Long member);
    Member updateMember(Long id, Member member);
}
