package org.example.testtasknerdy.service.impl;

import org.example.testtasknerdy.entity.Book;
import org.example.testtasknerdy.entity.Member;
import org.example.testtasknerdy.exception.BookNotAvailableException;
import org.example.testtasknerdy.exception.MemberHasBorrowedBooksException;
import org.example.testtasknerdy.exception.notFound.MemberNotFoundException;
import org.example.testtasknerdy.props.BookProperties;
import org.example.testtasknerdy.repository.MemberRepository;
import org.example.testtasknerdy.service.BookService;
import org.example.testtasknerdy.util.BookUtils;
import org.example.testtasknerdy.util.MemberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MemberServiceImplTest {

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookProperties bookProperties;

    @Autowired
    private MemberServiceImpl memberService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("GET 10 members")
    void findAllIsPresent() {
        int size = 10;
        when(memberRepository.findAll()).thenReturn(MemberUtils.generate(size));
        List<Member> members = memberService.findAll();
        assertEquals(size, members.size());
        assertTrue(members.stream()
                .allMatch(member ->
                        member.getName().contains(MemberUtils.NAME)
                )
        );
    }

    @Test
    @DisplayName("GET 0 members")
    void findAllIsEmpty() {
        int size = 0;
        when(memberRepository.findAll()).thenReturn(MemberUtils.generate(size));
        List<Member> members = memberService.findAll();
        assertEquals(size, members.size());
        assertTrue(members.stream()
                .allMatch(member ->
                        member.getName().contains(MemberUtils.NAME)
                )
        );
    }

    @Test
    @DisplayName("GET find member by id")
    void findById() {
        int size = 10;
        List<Member> members = MemberUtils.generate(size);
        when(memberRepository.findById(2L)).thenReturn(Optional.of(members.get(1)));

        Member member = memberService.findById(2L);

        assertNotNull(member);
        assertTrue(member.getName().contains(MemberUtils.NAME));
    }

    @Test
    @DisplayName("GET find member by id throw exception")
    void findByIdNotFoundException() {

        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(MemberNotFoundException.class, () -> memberService.findById(1L));
        assertTrue(exception.getMessage().toLowerCase().contains("Member Not Found".toLowerCase()));

    }

    @Test
    @DisplayName("POST add new member")
    void addMember() {
        Member member = MemberUtils.generate(1).get(0);
        when(memberRepository.save(member)).thenReturn(member);

        Member savedMember = memberService.addMember(member);
        assertNotNull(savedMember);
        assertTrue(member.getName().contains(MemberUtils.NAME));
    }

    @Test
    @DisplayName("DELETE delete user")
    void deleteMember() {
        Member member = MemberUtils.generate(1).get(0);
        member.setBooks(new ArrayList<>());
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        memberService.deleteMember(1L);

        verify(memberRepository).delete(member);
    }

    @Test
    @DisplayName("DELETE delete user")
    void deleteMemberBorrowingBooks() {
        Member member = new Member(1, "Ivan", LocalDateTime.now(), new ArrayList<>());
        Book borrowBook = new Book(2, "SDerr", "SErr Odf", 2, new ArrayList<>());

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(bookService.findById(2L)).thenReturn(borrowBook);

        member.getBooks().add(borrowBook);
        Exception exception = assertThrows(MemberHasBorrowedBooksException.class, () -> memberService.deleteMember(1L));
        assertTrue(exception.getMessage().toLowerCase().contains("Unable deletion.".toLowerCase()));

        verify(memberRepository, never()).delete(member);
    }

    @Test
    @DisplayName("PUT update member")
    void updateMember() {
        Member exist = MemberUtils.generate(1).get(0);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(exist));
        when(memberRepository.save(exist)).thenReturn(exist);

        Member memberToUpdate = new Member(2, "Name 2", LocalDateTime.now(), new ArrayList<>());
        Member result = memberService.updateMember(exist.getId(), memberToUpdate);
        assertNotNull(result);
        assertTrue(result.getName().contains(MemberUtils.NAME));
    }

    @Test
    @DisplayName("POST borrow book")
    void borrowBook() {
        Member member = MemberUtils.generate(1).get(0);
        Book book = BookUtils.generate(1).get(0);
        member.setBooks(new ArrayList<>());
        book.setMembers(new ArrayList<>());
        book.setAmount(5);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(bookService.findById(1L)).thenReturn(book);

        memberService.borrowBook(member.getId(), book.getId());

        assertEquals(4, book.getAmount());
        assertTrue(member.getBooks().contains(book));


        verify(memberRepository, times(1)).save(member);

    }


    @Test
    @DisplayName("POST borrow book is not available")
    void borrowBookWhenBookIsNotAvailable() {
        Member member = MemberUtils.generate(1).get(0);
        Book book = BookUtils.generate(1).get(0);
        book.setAmount(0);

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(bookService.findById(book.getId())).thenReturn(book);

        assertThrows(BookNotAvailableException.class, () -> memberService.borrowBook(member.getId(), book.getId()));

        verify(bookService, never()).update(book.getId(), book);
        verify(memberRepository, never()).save(member);
    }


    @Test
    @DisplayName("POST return book")
    void returnBookSuccessfully() {

        Member member = MemberUtils.generate(1).get(0);
        Book book = BookUtils.generate(1).get(0);

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(bookService.findById(book.getId())).thenReturn(book);

        memberService.borrowBook(member.getId(), book.getId());

        memberService.returnBook(member.getId(), book.getId());

        assertEquals(1, book.getAmount());
        assertTrue(member.getBooks().isEmpty());


    }

    @Test
    @DisplayName("POST return book when member did not borrow book")
    void returnBookWhenMemberDidNotBorrowBook() {
        Member member = MemberUtils.generate(1).get(0);
        Book book = BookUtils.generate(1).get(0);

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(bookService.findById(book.getId())).thenReturn(book);

        assertThrows(IllegalStateException.class, () -> memberService.returnBook(member.getId(), book.getId()));

        verify(bookService, never()).update(book.getId(), book);
        verify(memberRepository, never()).save(member);
    }

    @Test
    @DisplayName("GET all book by member name")
    void findAllBooksByMemberNameSuccessfully() {

        Member member = MemberUtils.generate(1).get(0);
        List<Book> borrowedBooks = BookUtils.generate(3);
        member.setBooks(borrowedBooks);

        when(memberRepository.findAllBooksByMemberName(member.getName())).thenReturn(member);


        List<Book> books = memberService.findAllBooksByMemberName(member.getName());

        assertEquals(3, books.size());
        assertEquals(borrowedBooks, books);
        verify(memberRepository, times(1)).findAllBooksByMemberName(member.getName());
    }

    @Test
    @DisplayName("GET all book by member name not found")
    void findAllBooksByMemberNameWhenMemberNotFound() {

        String memberName = "Unknown";
        when(memberRepository.findAllBooksByMemberName(memberName)).thenThrow(new MemberNotFoundException(memberName));

        Exception exception = assertThrows(MemberNotFoundException.class, () -> memberService.findAllBooksByMemberName(memberName));

        assertTrue(exception.getMessage().toLowerCase().contains("Member Not Found".toLowerCase()));
    }

}