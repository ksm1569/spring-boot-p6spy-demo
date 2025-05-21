package com.smsoft.springbootp6spydemo.controller;

import com.smsoft.springbootp6spydemo.entity.User;
import com.smsoft.springbootp6spydemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @PostMapping("/users")
    public String create(@RequestParam String name, @RequestParam String email) {
        userRepository.save(new User(name, email));
        return "redirect:/";
    }
}