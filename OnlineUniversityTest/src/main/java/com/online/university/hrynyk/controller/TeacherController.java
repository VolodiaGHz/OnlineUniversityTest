package com.online.university.hrynyk.controller;

import com.online.university.hrynyk.model.PriceData;
import com.online.university.hrynyk.model.User;
import com.online.university.hrynyk.repository.PriceRepo;
import com.online.university.hrynyk.service.LessonService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Controller
public class TeacherController {
    private final PriceRepo priceRepo;
    private final LessonService lessonService;

    public TeacherController(PriceRepo priceRepo, LessonService lessonService) {
        this.priceRepo = priceRepo;
        this.lessonService = lessonService;

    }

    @PostMapping("/createLesson")
    public String createLesson(@AuthenticationPrincipal User user,
                               @RequestParam Date date,
                               @RequestParam String time,
                               @RequestParam float price,
                               Map<String, Object> model) {
        if(!lessonService.createLessonByTeacher(user,time,date,price)){
            model.put("warning", "time slot not available!");
            List<PriceData> priceDataList = priceRepo.findAll();
            model.put("price",priceDataList);
            return "teacherProfile";
        }
        return "redirect:/profile";
    }

    @PostMapping("/acceptLesson")
    public String manageLessonStatus(@RequestParam String status,
                                     @RequestParam Long id){
        lessonService.acceptLesson(status,id);
        return "redirect:/profile";
    }
}
