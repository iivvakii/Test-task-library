package org.example.testtasknerdy.service;

import org.example.testtasknerdy.model.Book;

import java.util.List;

public interface BookService {
    Book add(Book book);
    Book update(Long id, Book book);
    List<Book> findAll();


    Book findById(Long id);
    void delete(Long id);
}
