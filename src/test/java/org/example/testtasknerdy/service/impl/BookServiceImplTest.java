package org.example.testtasknerdy.service.impl;

import org.example.testtasknerdy.dto.BorrowedBookInfo;
import org.example.testtasknerdy.entity.Book;
import org.example.testtasknerdy.entity.Member;
import org.example.testtasknerdy.exception.BookInUseException;
import org.example.testtasknerdy.exception.notFound.BookNotFoundException;
import org.example.testtasknerdy.repository.BookRepository;
import org.example.testtasknerdy.util.BookUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("GET 3 books")
    void findAllIsPresent() {
        int size = 3;
        when(bookRepository.findAll()).thenReturn(BookUtils.generate(size));
        List<Book> books = bookService.findAll();
        assertEquals(size, books.size());
        assertTrue(books.stream()
                .allMatch(book ->
                        book.getTitle().contains(BookUtils.TITLE) &&
                                book.getAuthor().contains(BookUtils.AUTHOR)
                )
        );

    }

    @Test
    @DisplayName("GET 0 books")
    void findAllIsEmpty() {
        int size = 0;
        when(bookRepository.findAll()).thenReturn(BookUtils.generate(size));
        List<Book> books = bookService.findAll();
        assertEquals(size, books.size());
        assertTrue(books.stream()
                .allMatch(book ->
                        book.getTitle().contains(BookUtils.TITLE) &&
                                book.getAuthor().contains(BookUtils.AUTHOR)
                )
        );

    }

    @Test
    @DisplayName("GET one book by id")
    void findById() {
        int size = 1;
        List<Book> books = BookUtils.generate(size);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(books.get(0)));

        Book book = bookService.findById(1L);
        assertNotNull(book);
        assertTrue(books.stream()
                .allMatch(result ->
                        result.getTitle().contains(BookUtils.TITLE) &&
                                result.getAuthor().contains(BookUtils.AUTHOR)
                )
        );
    }

    @Test
    @DisplayName("GET one book by id throw exception")
    void findByIdNotFoundException() {
        when(bookRepository.findById(1L)).thenThrow(new BookNotFoundException(1L));

        Exception exception = assertThrows(BookNotFoundException.class, () -> bookService.findById(1L));
        assertTrue(exception.getMessage().toLowerCase().contains("Book Not Found".toLowerCase()));
    }

    @Test
    @DisplayName("POST add new book")
    void addNewBook() {
        Book newBook = BookUtils.generate(1).get(0);
        when(bookRepository.findByTitleAndAuthor("Title 2", "Author 2")).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(newBook);

        Book book = bookService.add(newBook);
        assertNotNull(book);
        assertTrue(book.getTitle().contains(BookUtils.TITLE) && book.getAuthor().contains(BookUtils.AUTHOR));

    }

    @Test
    @DisplayName("POST add exist book")
    void addExistBook() {
        Book existBook = BookUtils.generate(1).get(0);
        when(bookRepository.findByTitleAndAuthor(existBook.getTitle(), existBook.getAuthor())).thenReturn(Optional.of(existBook));
        when(bookRepository.save(existBook)).thenReturn(existBook);

        Book newBook = new Book(2L, existBook.getTitle(), existBook.getAuthor());
        Book book = bookService.add(newBook);
        assertNotNull(book);
        assertEquals(2, book.getAmount());
    }

    @Test
    @DisplayName("PUT update book")
    void updateBook() {
        Book exist = BookUtils.generate(1).get(0);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(exist));
        when(bookRepository.save(any(Book.class))).thenReturn(exist);

        Book bookToUpdate = new Book("Title Upadate", "Author Update", 3, null);
        Book result = bookService.update(exist.getId(), bookToUpdate);
        assertNotNull(result);
        assertTrue(result.getTitle().contains("Title Upadate") && result.getAuthor().contains("Author Update"));
    }


    @Test
    @DisplayName("DELETE delete book")
    void deleteBook() {
        Book book = new Book(1L, "Title 1", "Author 1", 1, new ArrayList<>());

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.findByTitleAndAuthor("Title 1", "Author P 1")).thenReturn(Optional.of(book));

        bookService.delete(1L);

        if (book.getAmount() > 1) {
            assertEquals(0, book.getAmount());
            verify(bookRepository).save(book);
        } else {
            verify(bookRepository).delete(book);
        }
    }


    @Test
    @DisplayName("DELETE delete book in use")
    void deleteBookInUse() {
        Book book = new Book(1L, "Title One", "Author One", 0, new ArrayList<>());

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        book.getMembers().add(new Member());

        Exception exception = assertThrows(BookInUseException.class, () -> bookService.delete(1L));
        assertEquals("Unable deletion. Book with id = 1 as it is currently borrowed.", exception.getMessage());
        verify(bookRepository, never()).delete(book);
    }

    @Test
    @DisplayName("GET distinct borrowed books")
    void getDistinctBorrowedBookTitles() {
        List<String> expectedTitles = Arrays.asList("Book One", "Book Two", "Book Three");

        when(bookRepository.findDistinctBorrowedBookTitles()).thenReturn(expectedTitles);

        List<String> actualTitles = bookService.getDistinctBorrowedBookTitles();

        assertEquals(expectedTitles, actualTitles, "The distinct borrowed book titles should match the expected list.");
        verify(bookRepository, times(1)).findDistinctBorrowedBookTitles();
    }

    @Test
    @DisplayName("GET books with titles and counts")
    void findDistinctBorrowedBooksWithCount() {
        when(bookRepository.findDistinctBorrowedBooksWithCount()).thenReturn(Arrays.asList(
                new BorrowedBookInfo("Title One", 3L),
                new BorrowedBookInfo("Title Two", 5L)
        ));

        List<BorrowedBookInfo> borrowedBooks = bookService.findDistinctBorrowedBooksWithCount();
        assertEquals(2, borrowedBooks.size());
    }

    @Test
    @DisplayName("GET book by title and author found")
    void findByTitleAndAuthorBookFound() {

        String title = "Title One";
        String author = "Author One";
        Book expectedBook = new Book(1L, title, author, 1, null);

        when(bookRepository.findByTitleAndAuthor(title, author)).thenReturn(Optional.of(expectedBook));

        Book actualBook = bookService.findByTitleAndAuthor(title, author);

        assertEquals(expectedBook, actualBook);
        verify(bookRepository, times(1)).findByTitleAndAuthor(title, author);
    }

    @Test
    @DisplayName("GET book by title and author not found")
    void findByTitleAndAuthorBookNotFound() {

        String title = "Title One";
        String author = "Author One";

        when(bookRepository.findByTitleAndAuthor(title, author)).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            bookService.findByTitleAndAuthor(title, author);
        });

        assertTrue(exception.getMessage().toLowerCase().contains("Book Not Found".toLowerCase()));
        verify(bookRepository, times(1)).findByTitleAndAuthor(title, author);
    }
}