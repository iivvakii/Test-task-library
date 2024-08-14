package org.example.testtasknerdy.exception;

public class BookLimitExceedException extends RuntimeException {
    private static final String MESSAGE = "Limit Of Member's Book Exceeded. Allowed %d";

    public BookLimitExceedException(int limit) {
        super(String.format(MESSAGE, limit));
    }
}
