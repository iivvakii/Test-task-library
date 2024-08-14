package org.example.testtasknerdy.exception;

public class MemberHasBorrowedBooksException extends RuntimeException {
    private static final String MESSAGE = "Unable deletion. Member with id = %d has borrowed books.";

    public MemberHasBorrowedBooksException(final int id) {
        super(String.format(MESSAGE, id));
    }
}
