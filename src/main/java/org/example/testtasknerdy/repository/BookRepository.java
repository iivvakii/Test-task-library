package org.example.testtasknerdy.repository;

import org.example.testtasknerdy.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b where b.title = ?1 and b.author = ?2")
    Optional<Book> findByTitleAndAuthor(String title, String author);
}
