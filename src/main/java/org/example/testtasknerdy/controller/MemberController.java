package org.example.testtasknerdy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.example.testtasknerdy.entity.Book;
import org.example.testtasknerdy.entity.Member;
import org.example.testtasknerdy.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "Get all members",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public List<Member> getMembers() {
        return memberService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one member by id",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public Member getMember(@PathVariable long id) {
        return memberService.findById(id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add new member",
            responses = {
                    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public Member createMember(@RequestBody Member member) {
        return memberService.addMember(member);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete member",
            responses = {
                    @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public void deleteMember(@PathVariable long id) {
        memberService.deleteMember(id);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update member",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public Member updateMember(@PathVariable long id, @RequestBody Member member) {
        return memberService.updateMember(id, member);
    }

    @PostMapping("/{memberId}/borrow/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Member borrow book",
            responses = {
                    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public void borrowBook(@PathVariable long memberId, @PathVariable long bookId) {
        memberService.borrowBook(memberId, bookId);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Member return borrowed book",
            responses = {
                    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PostMapping("/{memberId}/return/{bookId}")
    public void returnBook(@PathVariable long bookId, @PathVariable long memberId) {
        memberService.returnBook(memberId, bookId);
    }


    @GetMapping("/{name}/borrowed-books")
    @Operation(summary = "Get all borrowed books by a specific member",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public List<Book> getBorrowedBooks(@PathVariable String name) {
        return memberService.findAllBooksByMemberName(name);
    }
}
