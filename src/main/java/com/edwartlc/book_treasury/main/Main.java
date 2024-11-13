package com.edwartlc.book_treasury.main;

import com.edwartlc.book_treasury.model.*;
import com.edwartlc.book_treasury.repository.AuthorRepository;
import com.edwartlc.book_treasury.repository.BookRepository;
import com.edwartlc.book_treasury.service.ApiService;
import com.edwartlc.book_treasury.service.DataConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private Scanner scanner = new Scanner(System.in);
    private ApiService apiService = new ApiService();
    private final String URL = "https://gutendex.com/books/";
    private DataConverter converter = new DataConverter();
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private List<Book> books;
    private List<Author> authors;

    public Main(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void displayMenu() {
        var option = -1;
        while (option != 0) {
            var menu = """
                \n-----------------------
                Escriba el número de la opción que desea ejecutar:
                1 - Buscar libro por título y guardarlo
                2 - Mostrar libros guardados
                3 - Mostrar autores guardados
                4 - Mostrar autores vivos en determinado año
                5 - Mostrar libros por idioma
                0 - Salir
                """;
            System.out.println(menu);
            try {
                option = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                option = 9;
                scanner.nextLine();
            }

            switch (option) {
                case 1:
                    searchBook();
                    break;
                case 2:
                    viewSavedBooks();
                    break;
                case 3:
                    viewSavedAuthors();
                    break;
                case 4:
                    viewLivingAuthors();
                    break;
                case 5:
                    viewBooksByLanguage();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("¡Opción no válida!");
            }
        }
    }

    private void searchBook() {
        System.out.println("Escriba el nombre del libro que quiere buscar: ");
        var bookTitle = scanner.nextLine();
        var json = apiService
                .getData(URL + "?search=" + bookTitle.replace(" ", "+"));
        var data = converter.getData(json, Data.class);
        Optional<BookData> searchedBook = data.results().stream()
                .filter(b -> b.title().toLowerCase().contains(bookTitle.toLowerCase()))
                .findFirst();
        if (searchedBook.isPresent()) {
            Optional<Book> searchBookInDB = bookRepository
                    .findByTitleContainsIgnoreCase(searchedBook.get().title());
            if (searchBookInDB.isPresent()) {
                System.out.println("\n¡El libro ya se encuentra en el repositorio!");
            } else {
                Book book = new Book(searchedBook.get());
                Author searchAuthor = authorRepository
                        .findByNameContainsIgnoreCase(searchedBook.get().author().get(0).name());
                if (searchAuthor != null) {
                    book.setAuthor(searchAuthor);
                } else {
                    Author author = new Author(searchedBook.get().author().get(0));
                    authorRepository.save(author);
                    book.setAuthor(author);
                }
                bookRepository.save(book);
                System.out.println("\n¡Agregado con éxito!");
                System.out.println(book);
            }
        } else {
            System.out.println("\n¡Libro no encontrado!");
        }
    }

    private void viewSavedBooks() {
        books = bookRepository.findAll();
        books.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .forEach(System.out::println);
    }

    private void viewSavedAuthors() {
        authors = authorRepository.findAll();
        authors.stream()
                .sorted(Comparator.comparing(Author::getName))
                .forEach(System.out::println);
    }

    private void viewLivingAuthors() {
        System.out.println("Escriba el año que quiere consultar:");
        try {
            int year = scanner.nextInt();
            scanner.nextLine();
            authors = authorRepository.authorsByYear(year);
            if (authors.size() != 0) {
                System.out.println("Autor(es) vivo(s) en el año " + year + ":");
                authors.stream()
                        .forEach(a -> System.out.println(a));
            } else {
                System.out.println("\nNinguno de los autores registrados estaba vivo " +
                        "en el año " + year + ".");
            }
        } catch (Exception e) {
            System.out.println("¡El valor ingresado no es válido!");
            scanner.nextLine();
        }
    }

    private void viewBooksByLanguage() {
        System.out.println("Escriba el idioma que quiere consultar: " +
                "\nes - Español" +
                "\nen - Inglés" +
                "\nfr - Francés" +
                "\npt - Portugués");
        try {
            var language = scanner.nextLine();
            books = bookRepository.booksByLanguage(language);
            if (books.size() != 0) {
                System.out.println("Libros encontrados:\n");
                books.stream()
                        .forEach(b -> System.out.println(b));
            } else {
                System.out.println("\nNo hay ningún libro en el repositorio en el idioma indicado.");
            }
        } catch (Exception e) {
            System.out.println("¡El valor ingresado no es válido!");
            scanner.nextLine();
        }
    }
}
