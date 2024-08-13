package org.example.testtasknerdy.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.testtasknerdy.model.Book;
import org.example.testtasknerdy.repository.BookRepository;
import org.example.testtasknerdy.service.BookService;
import org.springframework.stereotype.Service;

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
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public Book add(Book book) {
        Book book1 = findByTitleAndAuthor(book.getTitle(), book.getAuthor());
        if(book1 != null) {
            book1.setAmount(book1.getAmount() + 1);
            return update(book1.getId(), book1);
        }
        return bookRepository.save(book);
    }

    @Override
    public Book update(Long id, Book book) {
        if(findById(id) != null) book.setId(id);
        return bookRepository.save(book);
    }

    @Override
    public void delete(Long id) {
        if (findById(id) != null) {bookRepository.deleteById(id);}
    }

    private Book findByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleAndAuthor(title, author).get();
    }
}
