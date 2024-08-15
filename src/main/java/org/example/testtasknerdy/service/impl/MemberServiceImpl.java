package org.example.testtasknerdy.service.impl;

import org.example.testtasknerdy.entity.Book;
import org.example.testtasknerdy.entity.Member;
import org.example.testtasknerdy.exception.BookNotAvailableException;
import org.example.testtasknerdy.exception.MemberHasBorrowedBooksException;
import org.example.testtasknerdy.exception.notFound.MemberNotFoundException;
import org.example.testtasknerdy.props.BookProperties;
import org.example.testtasknerdy.repository.MemberRepository;
import org.example.testtasknerdy.service.BookService;
import org.example.testtasknerdy.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final BookService bookService;
    private final int borrowLimit;

    public MemberServiceImpl(MemberRepository memberRepository, BookService bookService, BookProperties bookProperties) {
        this.memberRepository = memberRepository;
        this.bookService = bookService;
        this.borrowLimit = bookProperties.getLimit();
    }



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
        if (member.getBooks().isEmpty()) {
            memberRepository.delete(findById(id));
        } else {
            throw new MemberHasBorrowedBooksException(id);
        }

    }

    @Override
    public Member updateMember(Long id, Member member) {
        Member memberToUpdate = findById(id);
        memberToUpdate.setName(member.getName());
        return memberRepository.save(memberToUpdate);
    }

    @Override
    @Transactional
    public void borrowBook(Long memberId, Long bookId) {
        Member member = findById(memberId);
        Book book = bookService.findById(bookId);

        if (book.getAmount() <= 0)
            throw new BookNotAvailableException(bookId);


        book.setAmount(book.getAmount() - 1);
        bookService.update(bookId, book);

        member.getBooks().add(book);
        memberRepository.save(member);
    }

    @Override
    public void returnBook(Long memberId, Long bookId) {
        Member member = findById(memberId);
        Book book = bookService.findById(bookId);

        if (!member.getBooks().contains(book)) {
            throw new IllegalStateException("Member did not borrow this book");
        }

        book.setAmount(book.getAmount() + 1);
        bookService.update(bookId, book);

        member.getBooks().remove(book);
        memberRepository.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAllBooksByMemberName(String memberName) {
        return memberRepository.findAllBooksByMemberName(memberName).getBooks();
    }
}
