package com.globant.library.repos;

import com.globant.library.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepo extends JpaRepository<Loan, String> {
}
