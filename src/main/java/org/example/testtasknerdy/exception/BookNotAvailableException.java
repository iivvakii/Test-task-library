package org.example.testtasknerdy.exception;


public class BookNotAvailableException extends RuntimeException {
    private static final String MESSAGE = "Book Not Available By Id %d";

    public BookNotAvailableException(Long bookId) {
        super(String.format(MESSAGE, bookId));
    }
}
