package com.globant.library.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String isbn;

    @Column(nullable = false)
    private String title;

    private int year;

    private int copies;

    private int availableCopies;

    private int lentCopies;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Author author;

    @ManyToOne
    private Publisher publisher;
}
