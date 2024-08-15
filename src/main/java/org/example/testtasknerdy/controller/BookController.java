package org.example.testtasknerdy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.example.testtasknerdy.dto.BorrowedBookInfo;
import org.example.testtasknerdy.entity.Book;
import org.example.testtasknerdy.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add new book",
            responses = {
                    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public Book addBook(@RequestBody Book book) {
        return bookService.add(book);
    }


    @GetMapping
    @Operation(summary = "Get all books",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public List<Book> getBooks() {
        return bookService.findAll();
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get one book by id",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public Book getBook(@PathVariable long id) {
        return bookService.findById(id);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete book",
            responses = {
                    @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public void deleteBook(@PathVariable long id) {
        bookService.delete(id);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update book",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public Book updateBook(@PathVariable long id, @RequestBody Book book) {
        return bookService.update(id, book);
    }


    @GetMapping("/borrowed/distinct-titles")
    @Operation(summary = "Get distinct borrowed books titles",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public List<String> getDistinctBorrowedBookTitles() {
        return bookService.getDistinctBorrowedBookTitles();
    }


    @GetMapping("/borrowed-summary")
    @Operation(summary = "Get borrowed books with count",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public List<BorrowedBookInfo> getBorrowedBooksSummary() {
        return bookService.findDistinctBorrowedBooksWithCount();
    }
}
