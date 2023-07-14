package com.example.springdataautomappingexercise;

import com.example.springdataautomappingexercise.model.dto.*;
import com.example.springdataautomappingexercise.service.GameService;
import com.example.springdataautomappingexercise.service.Impl.UserServiceImpl;
import com.example.springdataautomappingexercise.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final Scanner scanner;
    private final UserService userService;
    private final GameService gameService;
    private final UserServiceImpl userServiceImpl;

    public CommandLineRunnerImpl(UserService userService, GameService gameService, UserServiceImpl userServiceImpl) {
        this.userService = userService;
        this.gameService = gameService;
        this.userServiceImpl = userServiceImpl;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run(String... args) throws Exception {

        while(true) {
            System.out.println("Enter command: ");
            String[] command = scanner.next().split("\\|");

            switch (command[0]) {
                case "RegisterUser" -> userService.registerUser(
                        new UserRegisterDto(command[1],command[2],command[3],command[4]));

                case "LoginUser" -> userService.loginUser
                        (new UserLoginDto(command[1],command[2]));

                case "Logout" -> userService
                        .logout();

                case "AddGame" -> gameService
                        .addGame(new GameAddDto(command[1],new BigDecimal(command[2]), Double.parseDouble(command[3]),
                                command[4],command[5],command[6],command[7]));

                case "EditGame" -> gameService
                        .editGame(Long.parseLong(command[1]), new BigDecimal(command[2]),Double.parseDouble(command[3]));

                case "DeleteGame" -> gameService
                        .deleteGame(Long.parseLong(command[1]));

                case "AllGames" -> printAllGames();
                case "DetailGame" -> printDetailGame(command[1]);
                case "OwnedGames" -> printOwnedGames();

            }
        }

    }

    private void printOwnedGames() {
        if (!isLoggedUser()) return;
        List<OwnedGamesDto> ownedGames = userService.printOwnedGames(userServiceImpl.getLoggedInUser());
        if (ownedGames.isEmpty()) {
            System.out.println("You don't own any games");
            return;
        }

        ownedGames.forEach(System.out::println);
    }

    private void printDetailGame(String title) {
        if (!isLoggedUser()) return;
        DetailGameDto detailGameDto = gameService.detailGame(title);
        if (detailGameDto == null) {
            System.out.println("There is no such game");
            return;
        }
        System.out.println(detailGameDto);

    }

    private void printAllGames() {
        if (!isLoggedUser())
            return;
        List<AllGamesDto> allGames = gameService.allGames();
        if (allGames.isEmpty()) {
            return;
        }
        allGames.forEach(System.out::println);
    }
    private boolean isLoggedUser() {
        if (userServiceImpl.getLoggedInUser() == null) {
            System.out.println("Please login");
            return false;
        }
        return true;
    }
}
