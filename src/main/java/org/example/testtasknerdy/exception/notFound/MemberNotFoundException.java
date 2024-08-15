package org.example.testtasknerdy.exception.notFound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MemberNotFoundException extends RuntimeException {
    private static final String MESSAGE_ID = "Member Not Found By Id %d";
    private static final String MESSAGE_NAME = "Member Not Found By Name %s";

    public MemberNotFoundException(Long id) {
        super(String.format(MESSAGE_ID, id));
    }

    public MemberNotFoundException(String name) {
        super(String.format(MESSAGE_NAME, name));
    }
}
