package com.example.springintro.service;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface BookService {


    List<String> findAllBookTitlesWithAgeRestriction();

    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName);

    List<String> findAllBookTitlesWithAgeRestriction(AgeRestriction ageRestriction);

    List<String> findAllGoldenBookTitlesWithLessThan5000Copies();

    List<String> findAllBookTitlesWithPriceLessThan5MoreThan40();

    List<String> findNotReleasedBookTitlesInYear(int year);

    List<String> findAllBooksBeforeDate(LocalDate localDate);

    List<String> findAllBookTitlesWhereTitleContainsStr(String str);

    List<String> findAllTitlesWithAuthorsLastNameStartsWith(String startStr);

    int findCountOfBooksWithTitleLengthLongerThan(int titleLength);

    List<String> findBook(String title);
}
