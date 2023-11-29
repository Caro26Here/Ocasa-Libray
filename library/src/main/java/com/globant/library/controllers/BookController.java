package com.globant.library.controllers;

import com.globant.library.entities.Author;
import com.globant.library.entities.Book;
import com.globant.library.entities.Publisher;
import com.globant.library.exceptions.PersonalException;
import com.globant.library.services.AuthorService;
import com.globant.library.services.BookService;
import com.globant.library.services.PublisherService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/book") //localhost:8080/book
public class BookController {

    @Autowired
    private BookService bs;
    @Autowired
    private AuthorService as;
    @Autowired
    private PublisherService ps;

    @GetMapping("")
    public String home(){
        return "book.html";
    }

    @GetMapping("/entry") //localhost:8080/book/entry
    public String entry(ModelMap model){

        List<Author> authors = as.listAuthors();
        List<Publisher> publishers = ps.listPublishers();

        model.addAttribute("authors", authors);
        model.addAttribute("publishers", publishers);

        return "book_form.html";
    }

    @PostMapping("/entry") //localhost:8080/book/entry
    public String entry(@RequestParam(required = false) String title, @RequestParam(required = false) Integer year,
                        @RequestParam(required = false)  Integer copies, @RequestParam(required = false) String authorId,
                        @RequestParam(required = false) String publisherId, ModelMap model){
        try {

            bs.createBook(title, year, copies, authorId, publisherId);

            model.put("success", "The book has been saved successfully!");

            return "redirect:../book/list";

        } catch (PersonalException e){

            List<Author> authors = as.listAuthors();
            List<Publisher> publishers = ps.listPublishers();

            model.addAttribute("authors", authors);
            model.addAttribute("publishers", publishers);

            model.put("error", e.getMessage());

            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, "Error creating book", e);

            return "book_form.html";

        } catch (Exception e){

            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, "Error creating book", e);

            model.put("error", e.getMessage());

            return "book_form.html";
        }
    }

    @GetMapping("/list") //localhost:8080/book/list
    public String list(ModelMap model){

        List<Book> books = bs.listBooks();

        model.addAttribute("books", books);

        return "book_list.html";
    }

    @GetMapping("/edit/{isbn}")
    public String edit(@PathVariable String isbn, ModelMap model){

        List<Author> authors = as.listAuthors();
        List<Publisher> publishers = ps.listPublishers();

        model.addAttribute("authors", authors);
        model.addAttribute("publishers", publishers);
        model.put("book", bs.getBook(isbn));

        return "book_edit.html";
    }

    @PostMapping("/edit/{isbn}")
    public String edit(@PathVariable String isbn, String title, Integer year, Integer copies,
                       String authorId, String publisherId, ModelMap model){
        try{
            bs.updateBook(isbn, title, year, copies, authorId, publisherId);

            return "redirect:../list";

        }catch (PersonalException e){

            List<Author> authors = as.listAuthors();
            List<Publisher> publishers = ps.listPublishers();

            model.addAttribute("authors", authors);
            model.addAttribute("publishers", publishers);

            model.put("error", e.getMessage());

            return "book_edit.html";
        }
    }
}
