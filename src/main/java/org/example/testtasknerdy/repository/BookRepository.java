package org.example.testtasknerdy.repository;

import org.example.testtasknerdy.dto.BorrowedBookInfo;
import org.example.testtasknerdy.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b where b.title = ?1 and b.author = ?2")
    Optional<Book> findByTitleAndAuthor(String title, String author);

    @Query("select distinct b.title from Member m join m.books b")
    List<String> findDistinctBorrowedBookTitles();

    @Query("select b.title, COUNT(b.title) from Member m join m.books b group by b.title")
    List<BorrowedBookInfo> findDistinctBorrowedBooksWithCount();
}
