package org.example.testtasknerdy.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.testtasknerdy.dto.BorrowedBookInfo;
import org.example.testtasknerdy.entity.Book;
import org.example.testtasknerdy.exception.BookInUseException;
import org.example.testtasknerdy.exception.notFound.BookNotFoundException;
import org.example.testtasknerdy.repository.BookRepository;
import org.example.testtasknerdy.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override

    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }


    @Override
    @Transactional
    public Book add(Book newBook) {
        try {
            // Attempt to find the book by title and author
            Book existingBook = findByTitleAndAuthor(newBook.getTitle(), newBook.getAuthor());

            // If the book is found, update its amount
            existingBook.setAmount(existingBook.getAmount() + 1);
            log.info("Book found with title: {} and author: {}. Updated amount to: {}", existingBook.getTitle(), existingBook.getAuthor(), existingBook.getAmount());
            return existingBook;

        } catch (BookNotFoundException e) {
            // If the book is not found, save the new book
            log.info("Book not found with title: {} and author: {}. Saving new book.", newBook.getTitle(), newBook.getAuthor());
            return bookRepository.save(newBook);
        }
    }

    @Override
    public Book update(Long id, Book book) {
        Book bookToUpdate = findById(id);
        bookToUpdate.setAuthor(book.getAuthor());
        bookToUpdate.setTitle(book.getTitle());
        bookToUpdate.setAmount(book.getAmount());
        return bookRepository.save(bookToUpdate);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Book book = findById(id);
        if (book.getAmount() > 1) {
            book.setAmount(book.getAmount() - 1);
        } else {
            if (book.getMembers().isEmpty()) {
                bookRepository.delete(book);
            } else {
                throw new BookInUseException(id);
            }
        }


    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getDistinctBorrowedBookTitles() {
        return bookRepository.findDistinctBorrowedBookTitles();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowedBookInfo> findDistinctBorrowedBooksWithCount() {
        return bookRepository.findDistinctBorrowedBooksWithCount().stream()
                .map(result -> new BorrowedBookInfo(result.getTitle(), result.getCount()))
                .collect(Collectors.toList());
    }


    public Book findByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleAndAuthor(title, author).orElseThrow(() -> new BookNotFoundException(author, title));
    }
}
