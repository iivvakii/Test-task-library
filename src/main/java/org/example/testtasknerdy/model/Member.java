package org.example.testtasknerdy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    private LocalDateTime membershipDate;

    @ManyToMany
    @JoinTable(
            name = "member_book",
            joinColumns = @JoinColumn(name = "memeber_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )

    private List<Book> borrowedBooks = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        membershipDate = LocalDateTime.now();
    }
}
