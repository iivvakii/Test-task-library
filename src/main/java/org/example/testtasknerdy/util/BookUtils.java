package org.example.testtasknerdy.util;

import lombok.experimental.UtilityClass;
import org.example.testtasknerdy.entity.Book;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class BookUtils {
    public static final String TITLE = "Title";
    public static final String AUTHOR = "Author P";

    public List<Book> generate(int size) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Book book = new Book(i + 1,
                    String.format(TITLE.concat(" %d"), (i + 1)),
                    String.format(AUTHOR.concat(" %d"), (i + 1)),
                    i + 1,
                    new ArrayList<>());
            books.add(book);
        }
        return books;
    }
}
