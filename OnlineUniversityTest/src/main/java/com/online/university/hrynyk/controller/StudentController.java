package com.online.university.hrynyk.controller;

import com.online.university.hrynyk.model.Lesson;
import com.online.university.hrynyk.model.PriceData;
import com.online.university.hrynyk.model.User;
import com.online.university.hrynyk.repository.LessonRepo;
import com.online.university.hrynyk.repository.PriceRepo;
import com.online.university.hrynyk.repository.UserRepo;
import com.online.university.hrynyk.service.LessonService;
import com.online.university.hrynyk.service.LogicService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class StudentController {
    private final PriceRepo priceRepo;
    private final LogicService service;
    private final LessonRepo lessonRepo;
    private final UserRepo userRepo;
    private final LessonService lessonService;

    public StudentController(PriceRepo priceRepo, LogicService service, LessonRepo lessonRepo, UserRepo userRepo, LessonService lessonService) {
        this.priceRepo = priceRepo;
        this.service = service;
        this.lessonRepo = lessonRepo;
        this.userRepo = userRepo;
        this.lessonService = lessonService;
    }

    @PostMapping("/chooseSubject")
    public String chooseSubject(@RequestParam String subject,
                                Map<String, Object> model) {
        List<Object> dataList = new LinkedList<>();
        Iterable<PriceData> priceData = priceRepo.findAll();
        Map<String, Object> data = new HashMap<>();
        List<User> teachers = userRepo.findByPosition(service.findPosition(subject.toUpperCase()));
        for (User t : teachers) {
            System.out.println(t.getPosition());
        }
        data.put("price", priceData);
        data.put("teachers", teachers);
        dataList.add(data);
        model.put("createLesson", dataList);
        return "studentProfile";
    }

    @PostMapping("/signUpLesson")
    public String signUpLesson(@AuthenticationPrincipal User user,
                               @RequestParam Date date,
                               @RequestParam String time,
                               @RequestParam float price,
                               @RequestParam Long id,
                               Model model) {

         if (!lessonService.createLessonByStudent(user, time,date, price,id)){
            model.addAttribute("warning", "time slot not available!");
            return "studentProfile";
        }
        return "redirect:/profile";
    }



    @PostMapping("/cancelLesson")
    public String manageLessonStatus(@RequestParam Long id) {
        Lesson lesson = lessonRepo.findById(id).get();
        lessonRepo.delete(lesson);
        return "redirect:/profile";
    }
}
