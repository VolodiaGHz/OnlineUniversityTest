package com.online.university.hrynyk.controller;

import com.online.university.hrynyk.model.Position;
import com.online.university.hrynyk.model.Role;
import com.online.university.hrynyk.model.User;
import com.online.university.hrynyk.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/st_registration")
    public String registrationStudent() {
        return "registrationStudent";
    }

    @GetMapping("/th_registration")
    public String registrationTeacher(Model model) {
        Set<Position> positionSet = new HashSet<>(Arrays.asList(Position.values()));
        model.addAttribute("positionList", positionSet);
        return "registrationTeacher";
    }

    @PostMapping("/st_registration")
    public String addStudent(@Valid User student, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "registrationStudent";
        }else if (!userService.registrationUser(student, Role.STUDENT, Position.STUDENT)) {
                model.addAttribute("warning", "Email already exists!");
                return "registrationStudent";
            }
        return "redirect:/login";
    }

    @PostMapping("/th_registration")
    public String addUser(@RequestParam Position position,
                          @Valid User teacher,
                          Errors errors,
                          Model model) {
        if (errors.hasErrors()){
            return "registrationTeacher";
        }else if (!userService.registrationUser(teacher, Role.TEACHER, position)) {
            model.addAttribute("warning", "Email already exists!");
            return "registrationTeacher";
        }
        return "redirect:/login";
    }
}
