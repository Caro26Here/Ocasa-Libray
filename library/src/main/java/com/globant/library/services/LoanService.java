package com.globant.library.services;

import com.globant.library.entities.Book;
import com.globant.library.entities.Client;
import com.globant.library.entities.Loan;
import com.globant.library.exceptions.PersonalException;
import com.globant.library.repos.BookRepo;
import com.globant.library.repos.ClientRepo;
import com.globant.library.repos.LoanRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    @Autowired
    private LoanRepo loanRepo;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private BookService bookService;

    @Transactional
    public void createLoan(int loanDays, String bookId, String clientId) throws PersonalException {

        if (loanDays < 0){
            throw new PersonalException("The number of loan days entered cannot be a negative number.");
        }

        if (bookId == null || bookId.isEmpty()) {
            throw new PersonalException("The book ID entered cannot be null or empty.");
        }

        if (clientId == null || clientId.isEmpty()){
            throw new PersonalException("The client ID entered cannot be null or empty.");
        }

        Book bookSearched = bookRepo.findById(bookId).get();

        if (!bookSearched.isActive()){
            throw new PersonalException("The chosen book isn´t active at the moment.");
        }

        if(bookSearched.getAvailableCopies() > (bookSearched.getCopies() - bookSearched.getLentCopies())){
            throw new PersonalException("The chosen book isn't available at the moment.");
        }

        Client clientSearched = clientRepo.findById(clientId).get();

        if (!clientSearched.isActive()){
                throw new PersonalException("The client isn´t active at the moment.");
        }

        Loan newLoan = new Loan();

        newLoan.setBeginLoanDate(new Date());

        LocalDate auxDate = newLoan.getBeginLoanDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        auxDate = auxDate.plusDays(loanDays);

        newLoan.setReturnDate(Date.from(auxDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        bookSearched = bookService.lendCopy(bookSearched);

        newLoan.setBook(bookSearched);

        newLoan.setClient(clientSearched);

        loanRepo.save(newLoan);
    }

    public List<Loan> listLoans(){

        List<Loan> loansList = new ArrayList<>();

        loansList = loanRepo.findAll();

        return loansList;
    }

    @Transactional
    public void updateLoan(String id, int loanDays) throws PersonalException{

        if(id == null || id.isEmpty()){
            throw new PersonalException("The id entered cannot be null or empty.");
        }

        if (loanDays < 0){
            throw new PersonalException("The number of loan days entered cannot be a negative number.");
        }

        Optional<Loan> loanResponse = loanRepo.findById(id);
        Loan searchedLoan = new Loan();

        if(loanResponse.isPresent()){
            searchedLoan = loanResponse.get();

            Date auxDate = searchedLoan.getReturnDate();
            auxDate.setDate(auxDate.getDate() + loanDays);

            searchedLoan.setReturnDate(auxDate);

            loanRepo.save(searchedLoan);
        }
    }

    public Loan getLoan(String id){
        return loanRepo.getOne(id);
    }

    @Transactional
    public void deactivateLoan(String id) throws PersonalException{

        if( id == null){
            throw new PersonalException("The id entered cannot be null.");
        }

        Optional<Loan> loanResponse = loanRepo.findById(id);

        if(loanResponse.isPresent()){

            Loan foundLoan = loanResponse.get();
            foundLoan.setActive(false);
        }
    }

}
