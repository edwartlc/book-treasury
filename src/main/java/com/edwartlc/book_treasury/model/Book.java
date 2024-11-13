package com.edwartlc.book_treasury.model;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
    private String language;
    private Integer downloadCount;

    public Book() {}

    public Book(BookData bookData) {
        this.title = bookData.title();
        this.language = bookData.language().get(0);
        this.downloadCount = bookData.downloadCount();
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "-------- LIBRO --------" +
                "\nTítulo: " + title +
                "\nAutor: " + author.getName() +
                "\nIdioma: " + language +
                "\nDescargas: " + downloadCount +
                "\n-----------------------";
    }
}
