package com.globant.library.services;

import com.globant.library.entities.Author;
import com.globant.library.entities.Book;
import com.globant.library.entities.Publisher;
import com.globant.library.exceptions.PersonalException;
import com.globant.library.repos.AuthorRepo;
import com.globant.library.repos.BookRepo;
import com.globant.library.repos.PublisherRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private AuthorRepo authorRepo;
    @Autowired
    private PublisherRepo publisherRepo;

    @Transactional
    public void createBook(String title, int year, int copies, String authorId, String publisherId) throws PersonalException {

        if (title == null || title.isEmpty()){
            throw new PersonalException("The title entered cannot be null or empty.");
        }

        if (year < 0){
            throw new PersonalException("The year entered cannot be a negative number.");
        }

        if (copies < 1){
            throw new PersonalException("The number of copies cannot be less than 1.");
        }

        if (authorId == null || authorId.isEmpty()){
            throw new PersonalException("The author ID entered cannot be null or empty.");
        }

        if (publisherId == null || publisherId.isEmpty()){
            throw new PersonalException("The publisher ID entered cannot be null or empty.");
        }

        Book newBook = new Book();

        newBook.setTitle(title);
        newBook.setYear(year);
        newBook.setCopies(copies);
        newBook.setAvailableCopies(copies);
        newBook.setLentCopies(0);
        newBook.setActive(true);

        Author authorSearched = authorRepo.findById(authorId).get();
        newBook.setAuthor(authorSearched);

        Publisher publisherSearched = publisherRepo.findById(publisherId).get();
        newBook.setPublisher(publisherSearched);

        bookRepo.save(newBook);
    }

    public List<Book> listBooks(){

        List<Book> booksList = new ArrayList<>();

        booksList = bookRepo.findAll();

        return booksList;
    }

    @Transactional
    public void updateBook(String isbn, String title, int year, int copies,
                           String authorId, String publisherId) throws PersonalException{

        if (isbn == null){
            throw new PersonalException("The ISBN entered cannot be null.");
        }

        if (title == null || title.isEmpty()){
            throw new PersonalException("The title entered cannot be null or empty.");
        }

        if (year < 0){
            throw new PersonalException("The year entered cannot be a negative number.");
        }

        if (copies < 1){
            throw new PersonalException("The number of copies cannot be less than 1.");
        }

        if (authorId == null || authorId.isEmpty()){
            throw new PersonalException("The author ID entered cannot be null or empty.");
        }

        if (publisherId == null || publisherId.isEmpty()){
            throw new PersonalException("The publisher ID entered cannot be null or empty.");
        }

        Optional<Book> bookResponse = bookRepo.findById(isbn);
        Optional<Author> authorResponse = authorRepo.findById(authorId);
        Optional<Publisher> publisherResponse = publisherRepo.findById(publisherId);

        Author author = new Author();
        Publisher publisher = new Publisher();

        if(authorResponse.isPresent()){
            author = authorResponse.get();
        }

        if(publisherResponse.isPresent()){
            publisher = publisherResponse.get();
        }

        if(bookResponse.isPresent()){

            Book foundBook = bookResponse.get();

            foundBook.setTitle(title);
            foundBook.setYear(year);
            foundBook.setCopies(copies);
            foundBook.setAuthor(author);
            foundBook.setPublisher(publisher);

            bookRepo.save(foundBook);
        }
    }

    // go back and check that the return object is an Author, shouldn't it be a book?
    public Author getBook(String isbn){
        return authorRepo.getOne(isbn);
    }

    @Transactional
    public Book lendCopy(Book book){

        book.setLentCopies((book.getLentCopies() + 1));
        book.setAvailableCopies((book.getAvailableCopies() - 1));

        bookRepo.save(book);

        return book;
    }

    @Transactional
    public Book returnCopy(Book book){

        book.setLentCopies((book.getLentCopies() - 1));
        book.setAvailableCopies((book.getAvailableCopies() + 1));

        bookRepo.save(book);

        return book;
    }

    @Transactional
    public void deactivateBook(String isbn) throws PersonalException{

        if (isbn == null){
            throw new PersonalException("The ISBN entered cannot be null.");
        }

        Optional<Book> bookResponse = bookRepo.findById(isbn);

        if(bookResponse.isPresent()) {

            Book foundBook = bookResponse.get();
            foundBook.setActive(false);

            bookRepo.save(foundBook);
        }
    }

}
