package com.globant.library.services;

import com.globant.library.entities.Client;
import com.globant.library.entities.Loan;
import com.globant.library.exceptions.PersonalException;
import com.globant.library.repos.ClientRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepo clientRepo;

    @Transactional
    public void createClient(String firstName, String lastName, String phone, String email) throws PersonalException{

        if (firstName == null || firstName.isEmpty()){
            throw new PersonalException("The first name entered cannot be null or empty.");
        }

        if (lastName == null || lastName.isEmpty()){
            throw new PersonalException("The last name entered cannot be null or empty.");
        }

        if (phone == null || phone.isEmpty()){
            throw new PersonalException("The phone number entered cannot be null or empty.");
        }

        if (email == null || email.isEmpty()){
            throw new PersonalException("The email entered cannot be null or empty.");
        }

        Client newClient = new Client();

        newClient.setFirstName(firstName);
        newClient.setLastName(lastName);
        newClient.setPhone(phone);
        newClient.setEmail(email);
        newClient.setActive(true);

        clientRepo.save(newClient);
    }

    public List<Client> listClients(){

        List<Client> clientList = new ArrayList<>();

        clientList = clientRepo.findAll();

        return clientList;
    }

    @Transactional
    public void updateClient(String id, String firstName, String lastName, String phone, String email) throws PersonalException{

        if (id == null || id.isEmpty()){
            throw new PersonalException("The id entered cannot be null.");
        }

        if (firstName == null || firstName.isEmpty()){
            throw new PersonalException("The first name entered cannot be null or empty.");
        }

        if (lastName == null || lastName.isEmpty()){
            throw new PersonalException("The last name entered cannot be null or empty.");
        }

        if (phone == null || phone.isEmpty()){
            throw new PersonalException("The phone number entered cannot be null or empty.");
        }

        if (email == null || email.isEmpty()){
            throw new PersonalException("The email entered cannot be null or empty.");
        }

        Optional<Client> clientResponse = clientRepo.findById(id);

        if(clientResponse.isPresent()){

            Client foundClient = clientResponse.get();

            foundClient.setFirstName(firstName);
            foundClient.setLastName(lastName);
            foundClient.setPhone(phone);
            foundClient.setEmail(email);

            clientRepo.save(foundClient);
        }
    }

    public Client getClient(String id){
        return clientRepo.getOne(id);
    }

    @Transactional
    public void deactivateClient(String id) throws PersonalException{

        if( id == null){
            throw new PersonalException("The id entered cannot be null.");
        }

        Optional<Client> clientResponse = clientRepo.findById(id);

        if(clientResponse.isPresent()){

            Client foundClient = clientResponse.get();
            foundClient.setActive(false);
        }
    }
}
