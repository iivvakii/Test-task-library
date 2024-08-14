package org.example.testtasknerdy.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.testtasknerdy.entity.Book;
import org.example.testtasknerdy.exception.BookInUseException;
import org.example.testtasknerdy.exception.notFound.BookNotFoundException;
import org.example.testtasknerdy.repository.BookRepository;
import org.example.testtasknerdy.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
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
        return findByTitleAndAuthor(newBook.getTitle(), newBook.getAuthor())
                .map(book -> {
                    book.setAmount(book.getAmount() + 1);
                    return book;
                })
                .orElseGet(() -> bookRepository.save(newBook));
    }

    @Override
    public Book update(Long id, Book book) {
        Book bookToUpdate = findById(id);
        bookToUpdate.setAuthor(book.getAuthor());
        bookToUpdate.setTitle(book.getTitle());
        bookToUpdate.setAmount(book.getAmount());
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Book book = findById(id);
        if (book.getAmount() > 1) {
            book.setAmount(book.getAmount() - 1);
        } else {
            if (book.getMembersWhoBorrowed().isEmpty()) {
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

    private Optional<Book> findByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleAndAuthor(title, author);
    }
}
