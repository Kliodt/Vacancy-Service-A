package com.vacancy.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.vacancy.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

}
