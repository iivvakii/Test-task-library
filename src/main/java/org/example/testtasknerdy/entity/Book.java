package org.example.testtasknerdy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
//    @Pattern(regexp = "^[A-Z0-9]+$", message = "The name must start with a capital letter")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "Author is required")
//    @Pattern(regexp = "^[A-Z][a-z]+ [A-Z][a-z]+$", message = "Author must be two capitalized words separated by a space (e.g., Paulo Coelho)")
    private String author;


    private int amount = 1;

    @ManyToMany(mappedBy = "borrowedBooks")
    private List<Member> membersWhoBorrowed = new ArrayList<>();

}
