package com.globant.library.controllers;

import com.globant.library.entities.Author;
import com.globant.library.exceptions.PersonalException;
import com.globant.library.services.AuthorService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/author") //localhost:8080/author
public class AuthorController {

    @Autowired
    private AuthorService as;

    @GetMapping("")
    public String home(){
        return "author.html";
    }

    @GetMapping("/entry") //localhost:8080/author/entry
    public String signup(){
        return "author_form.html";
    }

    @PostMapping("/entry")
    public String entry(@RequestParam(required = true) String name){
        try {
            as.createAuthor(name);

            return "redirect:../author/list";

        } catch (PersonalException e) {

            Logger.getLogger(AuthorController.class.getName()).log(Level.SEVERE, null, e);

            return "author_form.html";
        }
    }

    @GetMapping("/list") //localhost:8080/author/list
    public String list(ModelMap model){

        List<Author> authors = as.listAuthors();

        model.addAttribute("authors", authors);

        return "author_list.html";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, ModelMap model){

        List<Author> authors = as.listAuthors();

        model.addAttribute("authors", authors);
        model.put("author", as.getAuthor(id));

        return "author_edit.html";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable String id, String name, ModelMap model){
        try{
            as.updateAuthor(id, name);

            return "redirect:../list";

        }catch (PersonalException e){

            List<Author> authors = as.listAuthors();
            model.addAttribute("authors", authors);

            model.put("error", e.getMessage());

            return "author_edit.html";
        }
    }
}
