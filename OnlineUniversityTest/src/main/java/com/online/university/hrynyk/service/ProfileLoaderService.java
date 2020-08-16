package com.online.university.hrynyk.service;

import com.online.university.hrynyk.model.Lesson;
import com.online.university.hrynyk.model.PriceData;
import com.online.university.hrynyk.model.Subject;
import com.online.university.hrynyk.model.User;
import com.online.university.hrynyk.repository.PriceRepo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProfileLoaderService {
    private final PriceRepo priceRepo;

    public ProfileLoaderService(PriceRepo priceRepo) {
        this.priceRepo = priceRepo;
    }

    public String getTeacherProfile(List<Lesson> lessonList, User user, Map<String, Object> model) {
        Iterable<PriceData> priceData = priceRepo.findAll();
        Map<String, Object> data = new HashMap<>();
        List<Object> list = new LinkedList<>();
        if (lessonList != null) {
            for (Lesson lesson : lessonList) {
                if (lesson.getTeacher_id() != null) {
                    if (lesson.getStatus().equals("none") && lesson.getTeacher_id().getId().equals(user.getId())) {
                        list = inputDataIntoList(lesson, list);
                    }
                }
            }
        }
        model.put("info", list);
        data.put("fullName", user.getFullName());
        data.put("position", user.getPosition().iterator().next().name());
        model.put("price", priceData);
        model.put("userInfo", data);
        return "teacherProfile";
    }

    public String getStudentProfile(List<Lesson> lessonList, User user, Map<String, Object> model) {
        Map<String, Object> data = new HashMap<>();
        List<Object> list = new LinkedList<>();
        for (Lesson lesson : lessonList) {
            if (lesson.getStudent_id() != null && lesson.getStudent_id().getId().equals(user.getId())) {
                if (!lesson.getStatus().equals("decline")) {
                    list = inputDataIntoList(lesson, list);
                }
            }
        }
        Iterable<Subject> subjects = Arrays.asList(Subject.values());
        model.put("info", list);
        data.put("subject", subjects);
        model.put("choose", data);
        data.put("fullName", user.getFullName());
        data.put("position", user.getPosition().iterator().next().name());
        model.put("userInfo", data);
        return "studentProfile";
    }

    public List<Object> inputDataIntoList(Lesson lesson, List<Object> list) {
        Map<String, Object> map = new HashMap<>();
        map.put("subject", lesson.getSubject().iterator().next().name());
        map.put("date", lesson.getDate());
        map.put("time", lesson.getTime());
        map.put("id", lesson.getId());
        if (lesson.getTeacher_id() != null) {
            map.put("teacherFullName", lesson.getTeacher_id().getFullName());
        }
        if (lesson.getStudent_id() != null) {
            map.put("studentFullName", lesson.getStudent_id().getFullName());
        }
        map.put("duration", lesson.getPrice_id().getDuration());
        map.put("price", lesson.getPrice_id().getPrice());
        list.add(map);
        return list;
    }
}
