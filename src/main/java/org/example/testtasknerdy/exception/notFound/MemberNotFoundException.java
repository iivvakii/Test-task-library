package org.example.testtasknerdy.exception.notFound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MemberNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Member Not Found By Id %d";

    public MemberNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
