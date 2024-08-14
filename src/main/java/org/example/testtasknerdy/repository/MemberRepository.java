package org.example.testtasknerdy.repository;

import org.example.testtasknerdy.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {


}
