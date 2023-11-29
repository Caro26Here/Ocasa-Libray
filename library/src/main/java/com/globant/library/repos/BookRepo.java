package com.globant.library.repos;

import com.globant.library.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, String> {

    @Query("SELECT b FROM Book b where b.title = :title")
    public Book searchByTitle(@Param("title") String title);

    @Query("SELECT b FROM Book b WHERE b.author.name = :authorName")
    public List<Book> searchBooksByAuthor(@Param("authorName") String authorName);
}
