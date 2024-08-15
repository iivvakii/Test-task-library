package org.example.testtasknerdy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(nullable = false)
    @NotBlank(message = "The name is required")
    @Size(min = 3, message = "The name must be at least 3 characters long")
    @Pattern(regexp = "^[A-Z].*", message = "The name must start with a capital letter")
    private String title;


    @Column(nullable = false)
    @NotBlank(message = "Author is required")
    @Pattern(regexp = "^[A-Z][a-z]+ [A-Z][a-z]+$", message = "Author must be two capitalized words separated by a space (e.g., Paulo Coelho)")
    private String author;


    private int amount = 1;

    @ManyToMany(mappedBy = "books")
    private List<Member> members = new ArrayList<>();


    public Book(long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public Book(String title, String author, int amount, List<Member> members) {
        this.title = title;
        this.author = author;
        this.amount = amount;
        this.members = members;
    }
}
