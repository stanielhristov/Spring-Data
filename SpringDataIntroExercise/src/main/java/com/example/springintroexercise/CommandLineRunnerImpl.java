package com.example.springintroexercise;

import com.example.springintroexercise.model.entity.Book;
import com.example.springintroexercise.service.AuthorService;
import com.example.springintroexercise.service.BookService;
import com.example.springintroexercise.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
    }
    @Override
    public void run(String... args) throws Exception {
        seedData();

        //printAllBookAfterYear(2000);

        //printALLAuthorNamesWithBooksWithReleaseDateBeforeYear(1990);

        //printAllAuthorsAndNumberOfTheirBooks();

        printAllBooksByAuthorNameOrderByReleaseDate("George", "Powell");

    }

    private void printAllBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        bookService.findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(firstName,lastName).forEach(System.out::println);
    }

    private void printAllAuthorsAndNumberOfTheirBooks() {
        authorService.getAllAuthorsOrderByCountOfTheirBooks().forEach(System.out::println);
    }

    private void printALLAuthorNamesWithBooksWithReleaseDateBeforeYear(int year) {
        bookService.findAllAuthorsWithBooksWithReleaseDateBeforeYear(year).forEach(System.out::println);

    }

    private void printAllBookAfterYear(int year) {
        bookService.findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle).forEach(System.out::println);

    }

    public void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }

}
