package com.example.springintroexercise.service;

import com.example.springintroexercise.model.entity.Author;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface AuthorService {
    void seedAuthors() throws IOException;

    Author getRandomAuthor();

    List<String> getAllAuthorsOrderByCountOfTheirBooks();
}
