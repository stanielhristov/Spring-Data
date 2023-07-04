package com.example.springintroexercise.service;

import com.example.springintroexercise.model.entity.Book;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName);

}
