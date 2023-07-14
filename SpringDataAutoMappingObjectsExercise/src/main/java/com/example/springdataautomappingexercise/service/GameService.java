package com.example.springdataautomappingexercise.service;

import com.example.springdataautomappingexercise.model.dto.AllGamesDto;
import com.example.springdataautomappingexercise.model.dto.DetailGameDto;
import com.example.springdataautomappingexercise.model.dto.GameAddDto;

import java.math.BigDecimal;
import java.util.List;

public interface GameService {
    void addGame(GameAddDto gameAddDto);

    void editGame(Long gameId, BigDecimal price, Double size);

    void deleteGame(Long gameId);

    List<AllGamesDto> allGames();

    DetailGameDto detailGame(String title);
}
