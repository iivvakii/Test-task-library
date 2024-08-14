package org.example.testtasknerdy.exception.notFound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Book Not Found By Id %d";

    public BookNotFoundException(final long id) {
        super(String.format(MESSAGE, id));
    }
}
