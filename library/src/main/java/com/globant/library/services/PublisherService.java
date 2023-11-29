package com.globant.library.services;

import com.globant.library.entities.Author;
import com.globant.library.entities.Client;
import com.globant.library.entities.Publisher;
import com.globant.library.exceptions.PersonalException;
import com.globant.library.repos.PublisherRepo;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {

    @Autowired
    PublisherRepo publisherRepo;

    @Transactional
    public void createPublisher(String name) throws PersonalException {

        if(name == null || name.isEmpty()){
            throw new PersonalException ("The name entered cannot be null or empty.");
        }else{
            Publisher newPublisher = new Publisher();

            newPublisher.setName(name);
            newPublisher.setActive(true);

            publisherRepo.save(newPublisher);
        }

    }

    public List<Publisher> listPublishers(){

        List<Publisher> publishersList = new ArrayList<>();

        publishersList = publisherRepo.findAll();

        return publishersList;
    }

    @Transactional
    public void updatePublisher(String id, String name){

        Optional<Publisher> response = publisherRepo.findById(id);

        if(response.isPresent()){

            Publisher publisher = response.get();

            publisher.setName(name);

            publisherRepo.save(publisher);
        }
    }

    public Publisher getPublisher(String id){
        return publisherRepo.getOne(id);
    }

    @Transactional
    public void deactivatePublisher(String id) throws PersonalException{

        if( id == null){
            throw new PersonalException("The id entered cannot be null.");
        }

        Optional<Publisher> publisherResponse = publisherRepo.findById(id);

        if(publisherResponse.isPresent()){

            Publisher foundPublisher = publisherResponse.get();
            foundPublisher.setActive(false);
        }
    }
}
