package com.example.springdataautomappingexercise.service;

import com.example.springdataautomappingexercise.model.dto.OwnedGamesDto;
import com.example.springdataautomappingexercise.model.dto.UserLoginDto;
import com.example.springdataautomappingexercise.model.dto.UserRegisterDto;
import com.example.springdataautomappingexercise.model.entity.User;

import java.util.List;

public interface UserService {
    void registerUser(UserRegisterDto userRegisterDto);

    void loginUser(UserLoginDto userLoginDto);

    void logout();
    List<OwnedGamesDto> printOwnedGames(User loggedInUser);
}
