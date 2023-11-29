package com.globant.library.services;

import com.globant.library.entities.Author;
import com.globant.library.entities.Client;
import com.globant.library.exceptions.PersonalException;
import com.globant.library.repos.AuthorRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    AuthorRepo authorRepo;

    @Transactional
    public void createAuthor(String name) throws PersonalException {

        if(name == null || name.isEmpty()){

            throw new PersonalException("The name entered cannot be null or empty.");

        }else{

            Author newAuthor = new Author();

            newAuthor.setName(name);
            newAuthor.setActive(true);

            authorRepo.save(newAuthor);
        }


    }

    public List<Author> listAuthors(){

        List<Author> authorsList = new ArrayList<>();

        authorsList = authorRepo.findAll();

        return authorsList;
    }

    @Transactional
    public void updateAuthor(String id, String name) throws PersonalException{

        if(id == null){
            throw new PersonalException("The ID entered cannot be null.");
        }

        if(name == null || name.isEmpty()){
            throw new PersonalException("The name entered cannot be null.");
        }

        Optional<Author> response = authorRepo.findById(id);

        if(response.isPresent()) {

            Author author = response.get();

            author.setName(name);

            authorRepo.save(author);
        }
    }

    public Author getAuthor(String id){
        return authorRepo.getOne(id);
    }

    @Transactional
    public void deactivateAuthor(String id) throws PersonalException{

        if( id == null){
            throw new PersonalException("The id entered cannot be null.");
        }

        Optional<Author> authorResponse = authorRepo.findById(id);

        if(authorResponse.isPresent()){

            Author foundAuthor = authorResponse.get();
            foundAuthor.setActive(false);
        }
    }

}
