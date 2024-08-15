package org.example.testtasknerdy.exception.notFound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {
    private static final String MESSAGE_ID = "Book Not Found By Id %d";
    private static final String MESSAGE_TITLE_AUTHOR = "Book Not Found By Name %s and Title %s";

    public BookNotFoundException(final long id) {
        super(String.format(MESSAGE_ID, id));
    }

    public BookNotFoundException(String author, String title) {
        super(String.format(MESSAGE_TITLE_AUTHOR, author, title));
    }
}
