package org.example.testtasknerdy.controller;

import lombok.RequiredArgsConstructor;
import org.example.testtasknerdy.dto.BorrowedBookInfoDto;
import org.example.testtasknerdy.entity.Book;
import org.example.testtasknerdy.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookService.add(book);
    }

    @GetMapping
    public List<Book> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable long id) {
        return bookService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable long id) {
        bookService.delete(id);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable long id, @RequestBody Book book) {
        return bookService.update(id, book);
    }

    @GetMapping("/borrowed/distinct-titles")
    public List<String> getDistinctBorrowedBookTitles() {
        return bookService.getDistinctBorrowedBookTitles();
    }

    @GetMapping("/borrowed-summary")
    public List<BorrowedBookInfoDto> getBorrowedBooksSummary() {
        return bookService.findDistinctBorrowedBooksWithCount();
    }
}
