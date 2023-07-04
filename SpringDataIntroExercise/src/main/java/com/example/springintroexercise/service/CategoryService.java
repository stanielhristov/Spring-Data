package com.example.springintroexercise.service;

import com.example.springintroexercise.model.entity.Category;

import java.io.IOException;
import java.util.Set;

public interface CategoryService {
    void seedCategories() throws IOException;

    Set<Category> getRandomCategories();
}
