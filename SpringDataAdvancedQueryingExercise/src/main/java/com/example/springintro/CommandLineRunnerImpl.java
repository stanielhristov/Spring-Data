package com.example.springintro;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final Scanner scanner;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService, Scanner scanner) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.scanner = scanner;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();

        //printAllBooksAfterYear(2000);
        //printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(1990);
        //printAllAuthorsAndNumberOfTheirBooks();
        //pritnALlBooksByAuthorNameOrderByReleaseDate("George", "Powell");

        System.out.println("Select exercise: ");
        int exerciseNumber = Integer.parseInt(scanner.nextLine());

        switch (exerciseNumber) {
            case 1 -> booksTitlesByAgeRestriction();
            case 2 -> goldenBook();
            case 3 -> bookByPrice();
            case 4 -> notReleasedBooks();
            case 5 -> booksReleasedBefore();
            case 6 -> authorsSearch();
            case 7 -> booksSearch();
            case 8 -> bookTitleSearch();
            case 9 -> countBooks();
            case 10 -> totalBookCopies();
            case 11 -> reducedBook();
            default -> System.out.println("No such exercise");
        }

    }

    private void reducedBook() {
        System.out.println("Enter title: ");
        String title = scanner.nextLine();
        bookService.findBook(title).forEach(System.out::println);
    }

    private void totalBookCopies() {
        authorService.findTotalBookCopies().forEach(System.out::println);
    }

    private void countBooks() {
        System.out.println("Enter title length");
        int titleLength = Integer.parseInt(scanner.nextLine());

        System.out.println(bookService.findCountOfBooksWithTitleLengthLongerThan(titleLength));
    }

    private void bookTitleSearch() {
        System.out.println("Enter author last name starts with str: ");
        String startStr = scanner.nextLine();

        bookService.findAllTitlesWithAuthorsLastNameStartsWith(startStr).forEach(System.out::println);

    }

    private void booksSearch() {
        System.out.println("Enter containing string: ");
        String str = scanner.nextLine();

        bookService.findAllBookTitlesWhereTitleContainsStr(str).forEach(System.out::println);

    }

    private void authorsSearch() {
        System.out.println("Enter first name ends with string");
        String endStr = scanner.nextLine();

        authorService.findAuthorFirstNameEndsWithStr(endStr).forEach(System.out::println);
    }

    private void booksReleasedBefore() {
        System.out.println("Enter date in format dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        bookService.findAllBooksBeforeDate(localDate)
                .forEach(System.out::println);
    }

    private void notReleasedBooks() {
        System.out.println("Enter year: ");
        int year = Integer.parseInt(scanner.nextLine());

        bookService.findNotReleasedBookTitlesInYear(year)
                .forEach(System.out::println);
    }

    private void bookByPrice() {
        bookService.findAllBookTitlesWithPriceLessThan5MoreThan40()
                .forEach(System.out::println);
    }

    private void goldenBook() {
        bookService.findAllGoldenBookTitlesWithLessThan5000Copies().forEach(System.out::println);
    }

    private void booksTitlesByAgeRestriction() {
        System.out.println("Enter age restriction: ");
        AgeRestriction ageRestriction = AgeRestriction.valueOf(scanner.nextLine().toUpperCase());

        bookService.findAllBookTitlesWithAgeRestriction(ageRestriction)
                .forEach(System.out::println);

    }

    private void pritnALlBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        bookService
                .findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsAndNumberOfTheirBooks() {
        authorService
                .getAllAuthorsOrderByCountOfTheirBooks()
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(int year) {
        bookService
                .findAllAuthorsWithBooksWithReleaseDateBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksAfterYear(int year) {
        bookService
                .findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }
}
