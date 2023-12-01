package com.globant.library.controllers;

import com.globant.library.entities.Client;
import com.globant.library.entities.Loan;
import com.globant.library.exceptions.PersonalException;
import com.globant.library.services.ClientService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Permission;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService cs;

    @GetMapping("/entry")
    public String entry(ModelMap model){

        return "client_form.html";
    }

    @PostMapping("/entry")
    public String entry(@RequestParam(required = false) String firstName,@RequestParam(required = false) String lastName,
                        @RequestParam(required = false) String phone, @RequestParam(required = false) String email,
                        ModelMap model){

        try{
            cs.createClient(firstName, lastName, phone, email);

//            model.put("success", "The client has been created!");

            return "redirect:../home";

        }catch (PersonalException e){

            model.put("error", e.getMessage());

            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, "Error creating book", e);

            return "client_form.html";
        }
    }

    @GetMapping("/list")
    public String list(ModelMap model){

        List<Client> clients = cs.listClients();

        model.addAttribute("clients", clients);

        return "client_list";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(required = false) String id, ModelMap model){

        List<Client> clients = cs.listClients();

        model.addAttribute("clients", clients);
        model.put("client", cs.getClient(id));

        return "client_edit.html";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(required = false) String id, @PathVariable(required = false) String firstName,
                       @PathVariable(required = false) String lastName, @PathVariable(required = false) String phone,
                       @PathVariable(required = false) String email, ModelMap model){
        try{
            cs.updateClient(id, firstName, lastName, phone, email);

            return "redirect:../list";

        }catch (Exception e){

            List<Client> clients = cs.listClients();

            model.addAttribute("clients", clients);

            model.put("error", e.getMessage());

            return "client_edit.html";
        }

    }
}
