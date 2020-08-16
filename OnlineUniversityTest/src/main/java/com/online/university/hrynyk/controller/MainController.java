package com.online.university.hrynyk.controller;

import com.online.university.hrynyk.model.Lesson;
import com.online.university.hrynyk.model.Role;
import com.online.university.hrynyk.model.Subject;
import com.online.university.hrynyk.model.User;
import com.online.university.hrynyk.repository.LessonRepo;
import com.online.university.hrynyk.service.ProfileLoaderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class MainController {
    private final LessonRepo lessonRepo;
    private final ProfileLoaderService profileLoader;

    public MainController(LessonRepo lessonRepo, ProfileLoaderService profileLoader) {
        this.lessonRepo = lessonRepo;
        this.profileLoader = profileLoader;
    }

    @GetMapping("/")
    public String mainPage(Map<String, Object> model) {
        Set<Subject> subjects = new HashSet<>(Arrays.asList(Subject.values()));
        model.put("subjectList", subjects);
        List<Object> list = new LinkedList<>();
        List<Lesson> lessons = lessonRepo.findAll();
        for (Lesson lesson : lessons) {
            String lessonStatus = lesson.getStatus();
            if (lesson.getTeacher_id() != null) {
                if (!lessonStatus.equals("none") && !lessonStatus.equals("decline")) {
                    list = profileLoader.inputDataIntoList(lesson, list);
                }
            }
        }
        Map<String, Object> infoLessons = new HashMap<>();
        infoLessons.put("info", list);
        model.put("lessons", infoLessons);
        return "main";
    }

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal User user,
                             Map<String, Object> model) {
        String userRole = user.getRoles().toString().substring(1, user.getRoles().toString().length() - 1);
        List<Lesson> lessonList = lessonRepo.findAll();
        if (userRole.equals(Role.TEACHER.toString())) {
            return profileLoader.getTeacherProfile(lessonList, user, model);
        }
        return profileLoader.getStudentProfile(lessonList, user, model);
    }


}
