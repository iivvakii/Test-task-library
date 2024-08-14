package org.example.testtasknerdy.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.testtasknerdy.entity.Book;
import org.example.testtasknerdy.entity.Member;
import org.example.testtasknerdy.exception.BookNotAvailableException;
import org.example.testtasknerdy.exception.notFound.MemberNotFoundException;
import org.example.testtasknerdy.repository.MemberRepository;
import org.example.testtasknerdy.service.BookService;
import org.example.testtasknerdy.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
    }

    @Override
    public Member addMember(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public void deleteMember(Long id) {
        Member member = findById(id);
        if (member.getBorrowedBooks().isEmpty()) {
            memberRepository.delete(findById(id));
        } else {
            throw new BookNotAvailableException(id);
        }
    }

    @Override
    public Member updateMember(Long id, Member member) {
        Member memberToUpdate = findById(id);
        memberToUpdate.setName(member.getName());
        return memberRepository.save(member);
    }

    @Override
    public void borrowBook(Long memberId, Long bookId) {
        Member member = findById(memberId);
        Book book = bookService.findById(bookId);

        if (book.getAmount() <= 0)
            throw new BookNotAvailableException(bookId);

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

        if (!member.getBorrowedBooks().contains(book)) {
            throw new IllegalStateException("Member did not borrow this book");
        }

        book.setAmount(book.getAmount() + 1);
        bookService.update(bookId, book);

        member.getBorrowedBooks().remove(book);
        memberRepository.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAllBooksByMemberName(String memberName) {
        return memberRepository.findAllBooksByMemberName(memberName).getBorrowedBooks();
    }
}
