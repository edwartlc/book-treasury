package com.edwartlc.book_treasury.repository;

import com.edwartlc.book_treasury.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitleContainsIgnoreCase(String bookTitle);
//    Java Persistence Query Language (JPA)
    @Query("SELECT b FROM Book b WHERE b.language = :language")
    List<Book> booksByLanguage(String language);
}
