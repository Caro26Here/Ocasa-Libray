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
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date beginLoanDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date returnDate;

    @OneToOne
    @JoinColumn(nullable = false)
    private Book book;

    @OneToOne
    @JoinColumn(nullable = false)
    private Client client;

    @Column(nullable = false)
    private boolean active;
}
