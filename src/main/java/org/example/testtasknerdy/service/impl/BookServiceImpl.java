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
        Optional<Book> book1 = findByTitleAndAuthor(book.getTitle(), book.getAuthor());
        if (book1.isPresent()) {
            book1.get().setAmount(book1.get().getAmount() + 1);
            return update(book1.get().getId(), book1.get());
        }
        book.setAmount(book.getAmount() + 1);
        return bookRepository.save(book);
    }

    @Override
    public Book update(Long id, Book book) {
        if(findById(id) != null) book.setId(id);
        return bookRepository.save(book);
    }

    @Override
    public void delete(Long id) {
        Book book = findById(id);
        Optional<Book> book1 = findByTitleAndAuthor(book.getTitle(), book.getAuthor());
        if (book1.isPresent() && book1.get().getAmount() > 0) {
            book1.get().setAmount(book1.get().getAmount() - 1);
            update(book1.get().getId(), book1.get());
        } else {
            bookRepository.deleteById(id);
        }

    }

    private Optional<Book> findByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleAndAuthor(title, author);
    }
}
