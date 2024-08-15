package org.example.testtasknerdy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.testtasknerdy.entity.listener.BookLimitListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({AuditingEntityListener.class, BookLimitListener.class})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @CreatedDate
    private LocalDateTime membershipDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "member_book",
            joinColumns = @JoinColumn(name = "memeber_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
//    @JsonIgnore
    private List<Book> books = new ArrayList<>();

    public Member(String name, LocalDateTime membershipDate, List<Book> books) {
        this.name = name;
        this.membershipDate = membershipDate;
        this.books = books;
    }
}
