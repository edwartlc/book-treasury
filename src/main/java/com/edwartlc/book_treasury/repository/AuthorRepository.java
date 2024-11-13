package com.edwartlc.book_treasury.repository;

import com.edwartlc.book_treasury.model.Author;
import com.edwartlc.book_treasury.model.AuthorData;
import com.edwartlc.book_treasury.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByNameContainsIgnoreCase(String name);
//    Java Persistence Query Language (JPA)
    @Query("SELECT a FROM Author a WHERE a.birthYear <= :year AND " +
            "a.deathYear >= :year")
    List<Author> authorsByYear(int year);
}
