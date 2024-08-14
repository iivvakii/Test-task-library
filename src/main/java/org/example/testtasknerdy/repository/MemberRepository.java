package org.example.testtasknerdy.repository;

import org.example.testtasknerdy.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from Member m where m.name = ?1")
    Member findAllBooksByMemberName(String name);
}
