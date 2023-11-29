package com.globant.library.controllers;

import com.globant.library.entities.Author;
import com.globant.library.entities.Publisher;
import com.globant.library.exceptions.PersonalException;
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
@RequestMapping("/publisher") //localhost:8080/publisher
public class PublisherController {

    @Autowired
    private PublisherService ps;

    @GetMapping("")
    public String home(){
        return "publisher.html";
    }

    @GetMapping("/entry") //localhost:8080/publisher/entry
    public String signup(){
        return "publisher_form.html";
    }

    @Transactional
    @PostMapping("/entry")
    public String entry(@RequestParam(required = true) String name){

        try {
            ps.createPublisher(name);

            return "redirect:../publisher/list";

        } catch (PersonalException e) {
            Logger.getLogger(PublisherController.class.getName()).log(Level.SEVERE, null, e);

            return "publisher_form.html";
        }


    }

    @GetMapping("/list") //localhost:8080/author/list
    public String list(ModelMap model){

        List<Publisher> publishers = ps.listPublishers();

        model.addAttribute("publishers", publishers);

        return "publisher_list.html";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, ModelMap model){

        List<Publisher> publishers = ps.listPublishers();

        model.addAttribute("publishers", publishers);
        model.put("publisher", ps.getPublisher(id));

        return "publisher_edit.html";
    }

    @Transactional
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable String id, String name, ModelMap model){
        try{
            ps.updatePublisher(id, name);

            return "redirect:../list";

        }catch (Exception e){

            List<Publisher> publishers = ps.listPublishers();

            model.addAttribute("publishers", publishers);

            model.put("error", e.getMessage());

            return "publisher_edit.html";
        }
    }
}
