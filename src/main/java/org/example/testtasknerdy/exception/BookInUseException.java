package org.example.testtasknerdy.exception;

public class BookInUseException extends RuntimeException {
    private static final String MESSAGE = "Unable deletion. Book with id = %d as it is currently borrowed.";

    public BookInUseException(final long id) {
        super(String.format(MESSAGE, id));
    }
}
