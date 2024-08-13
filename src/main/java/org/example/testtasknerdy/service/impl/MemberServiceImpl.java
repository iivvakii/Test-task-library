package org.example.testtasknerdy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.testtasknerdy.model.Book;
import org.example.testtasknerdy.model.Member;
import org.example.testtasknerdy.repository.MemberRepository;
import org.example.testtasknerdy.service.BookService;
import org.example.testtasknerdy.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final BookService bookService;

    @Value("${book.limit}")
    private int borrowLimit;

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

    @Override
    public void borrowBook(Long memberId, Long bookId) {
        Member member = findById(memberId);
        Book book = bookService.findById(bookId);

        if (member == null)
            throw new EntityNotFoundException("Member not found");

        if (book == null)
            throw new EntityNotFoundException("Book not found");


        if (book.getAmount() <= 0)
            throw new IllegalStateException("Book not available");

        if (member.getBorrowedBooks().size() >= borrowLimit)
            throw new IllegalStateException("Too many borrowed books");

        book.setAmount(book.getAmount() - 1);
        bookService.update(bookId, book);

        member.getBorrowedBooks().add(book);
        memberRepository.save(member);
    }

    @Override
    public void returnBook(Long memberId, Long bookId) {
        Member member = findById(memberId);
        Book book = bookService.findById(bookId);

        if (member == null)
            throw new EntityNotFoundException("Member not found");

        if (book == null)
            throw new EntityNotFoundException("Book not found");

        if (!member.getBorrowedBooks().contains(book)) {
            throw new IllegalStateException("Member did not borrow this book");
        }

        book.setAmount(book.getAmount() + 1);
        bookService.update(bookId, book);

        member.getBorrowedBooks().remove(book);
        memberRepository.save(member);
    }
}
