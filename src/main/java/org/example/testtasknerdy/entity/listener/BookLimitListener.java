package org.example.testtasknerdy.entity.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.example.testtasknerdy.entity.Member;
import org.example.testtasknerdy.exception.BookLimitExceedException;
import org.example.testtasknerdy.props.BookProperties;
import org.springframework.stereotype.Component;

@Component
public class BookLimitListener {
    private final int borrowLimit;

    public BookLimitListener(BookProperties bookProperties) {
        this.borrowLimit = bookProperties.getLimit();
    }

    @PrePersist
    @PreUpdate
    public void validateBorrowLimit(Member member) {
        if (member.getBooks().size() > borrowLimit) {
            throw new BookLimitExceedException(borrowLimit);
        }
    }
}
