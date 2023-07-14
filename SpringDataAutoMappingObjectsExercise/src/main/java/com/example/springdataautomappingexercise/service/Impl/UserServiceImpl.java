package com.example.springdataautomappingexercise.service.Impl;

import com.example.springdataautomappingexercise.model.dto.OwnedGamesDto;
import com.example.springdataautomappingexercise.model.dto.UserLoginDto;
import com.example.springdataautomappingexercise.model.dto.UserRegisterDto;
import com.example.springdataautomappingexercise.model.entity.User;
import com.example.springdataautomappingexercise.repository.UserRepository;
import com.example.springdataautomappingexercise.service.UserService;
import com.example.springdataautomappingexercise.util.ValidationUtil;
import jakarta.validation.ConstraintViolation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private User loggedInUser;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void registerUser(UserRegisterDto userRegisterDto) {
        if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            System.out.println("Wrong confirm password");
            return;
        }


        Set<ConstraintViolation<UserRegisterDto>> violations =
                validationUtil.violation(userRegisterDto);

        if (!violations.isEmpty()) {
            violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);

            return;
        }
        User user = modelMapper.map(userRegisterDto, User.class);

        userRepository.save(user);
    }

    @Override
    public void loginUser(UserLoginDto userLoginDto) {
        if (isValidEntity(userLoginDto)) return;

        Optional<User> user = userRepository.findByEmailAndPassword(userLoginDto.getEmail(), userLoginDto.getPassword());
        if (user.isEmpty()) {
            System.out.println("Incorrect username / password");
            return;
        }
        loggedInUser = user.get();
        System.out.printf("Successfully logged in %s%n", user.get().getFullName());

    }

    private <E> boolean isValidEntity(E entity) {
        Set<ConstraintViolation<E>> violations = validationUtil.violation(entity);
        if (!violations.isEmpty()) {
            violations.stream().map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            return true;
        }
        return false;
    }

    @Override
    public void logout() {
        if (loggedInUser == null) {
            System.out.println("Cannot logout. No user was logged in");
        } else {
            loggedInUser = null;
        }
    }

    @Override
    public List<OwnedGamesDto> printOwnedGames(User loggedInUser) {

        return userRepository.findAllByUser(loggedInUser.getId()).stream()
                .map(game -> modelMapper.map(game, OwnedGamesDto.class))
                .collect(Collectors.toList());

    }


    public User getLoggedInUser() {
        return loggedInUser;
    }
}
