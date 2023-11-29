package com.globant.library.controllers;

import com.globant.library.entities.Book;
import com.globant.library.entities.Client;
import com.globant.library.entities.Loan;
import com.globant.library.exceptions.PersonalException;
import com.globant.library.services.BookService;
import com.globant.library.services.ClientService;
import com.globant.library.services.LoanService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/rental")
public class LoanController {

    @Autowired
    private ClientService cs;

    @Autowired
    private BookService bs;

    @Autowired
    private LoanService ls;

    @GetMapping("/entry")
    public String entry(ModelMap model){

        List<Client> clients = cs.listClients();
        List<Book> books = bs.listBooks();

        model.addAttribute("clients", clients);
        model.addAttribute("books", books);

        return "rental_form.html";
    }

    @PostMapping("/entry")
    public String entry(@RequestParam(required = false) String clientId, @RequestParam(required = false) String bookId,
                        @RequestParam(required = false) Integer loanDays, ModelMap model){

        try{
            ls.createLoan(loanDays, bookId, clientId);

            model.put("success", "The loan has been created!");

            return "redirect:../rental/list";

        } catch (PersonalException e){

            List<Client> clients = cs.listClients();
            List<Book> books = bs.listBooks();

            model.addAttribute("clients", clients);
            model.addAttribute("books", books);

            model.put("error", e.getMessage());

            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, "Error creating rental", e);

            return "rental_form.html";
        }
    }

    @GetMapping("/list")
    public String list(ModelMap model) {

        List<Loan> loans = ls.listLoans();

        model.addAttribute("loans", loans);

        return "rental_list.html";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(required = false) String id, ModelMap model){

        List<Loan> loans = ls.listLoans();

        model.addAttribute("loans", loans);
        model.put("loan", ls.getLoan(id));

        return "rental_edit.html";
    }

    @PostMapping("/edit/{id}")
    public String edit(@RequestParam(required = false) String id, @RequestParam(required = false) Integer loanDays, ModelMap model){
        try{
            ls.updateLoan(id, loanDays);

            return "redirect:../list";

        }catch (PersonalException e){

            List<Loan> loans = ls.listLoans();

            model.addAttribute("loans", loans);

            model.put("loan", ls.getLoan(id));
            model.put("error", e.getMessage());

            return "rental_edit.html";
        }
    }
}
