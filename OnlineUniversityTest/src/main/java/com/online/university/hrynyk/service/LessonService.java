package com.online.university.hrynyk.service;

import com.online.university.hrynyk.model.*;
import com.online.university.hrynyk.repository.LessonRepo;
import com.online.university.hrynyk.repository.PriceRepo;
import com.online.university.hrynyk.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.sql.Time;
import java.util.Collections;

@Service
public class LessonService {
    private final PriceRepo priceRepo;
    private final LogicService logicService;
    private final LessonRepo lessonRepo;
    private final UserRepo userRepo;
    private final MailSender mailSender;

    public LessonService(PriceRepo priceRepo, LogicService logicService, LessonRepo lessonRepo, UserRepo userRepo, MailSender mailSender) {
        this.priceRepo = priceRepo;
        this.logicService = logicService;
        this.lessonRepo = lessonRepo;
        this.userRepo = userRepo;
        this.mailSender = mailSender;
    }

    public boolean createLessonByTeacher(User user, String time, Date date, float price) {
        String positionToString = user.getPosition().toString().substring(1, user.getPosition().toString().length() - 1);
        Subject subject = logicService.findSubjectField(positionToString);
        Lesson lesson = checkAvailableLesson(time,date,price,user);
        if (lesson!=null) {
            lesson.setTeacher_id(user);
            lesson.setStatus("accepted");
            lesson.setSubject(Collections.singleton(subject));
            lessonRepo.save(lesson);
            return true;
        }
        return false;
    }

    public void acceptLesson(String status, Long id) {
        Lesson lesson = lessonRepo.findById(id).get();
        System.out.println(status);
        if (status.equals("accept")) {
            lesson.setStatus("accepted");
            lessonRepo.save(lesson);
        }else if(status.equals("decline")) {
            lesson.setStatus("decline");
            lessonRepo.save(lesson);
        }
    }

    public Lesson checkAvailableLesson(String time, Date date, float price,User user){
        time = time + ":00";
        Time timeSql = Time.valueOf(time);
        PriceData priceData = priceRepo.findByPrice(price);
        if (logicService.ifTimeSlotExists(user, date, timeSql )
                || !logicService.ifTimeSlotAvailable(date, timeSql, user)) {
            return null;
        }
        Lesson lesson = new Lesson(date, timeSql, priceData);
        return lesson;
    }

    public boolean createLessonByStudent(User user, String time, Date date, float price,Long id) {
        User teacher = userRepo.findById(id).get();
        String positionToString = teacher.getPosition().toString().substring(1, user.getPosition().toString().length() - 1);
        Lesson lesson = checkAvailableLesson(time,date,price, teacher);
        if (lesson!=null) {
            lesson.setStudent_id(user);
            Subject subject = logicService.findSubjectField(positionToString);
            lesson.setSubject(Collections.singleton(subject));
            lesson.setTeacher_id(teacher);
            lesson.setStatus("none");
            lessonRepo.save(lesson);
            if (!StringUtils.isEmpty(user.getEmail())){
                String messageToTeacher = String.format(
                        "Hello "+ teacher.getFullName() + "\n" +
                                "Someone booked lesson to you. Please visit this link to approve/decline \n" +
                                "http://localhost:8080/profile",
                        user.getUsername()
                );
                String messageToStudent = String.format(
                        "Hello "+ user.getFullName() + "\n" +
                                "Lesson to"+ teacher.getFullName()+" booked successfully. \n" +
                                "Please visit this link to cancel the lesson\n" +
                                "http://localhost:8080/profile",
                        user.getUsername()
                );
                mailSender.sendMail(teacher.getEmail(), "Someone booked a lesson ", messageToTeacher);
                mailSender.sendMail(user.getEmail(), "Lesson booked succeed", messageToStudent);

            }
            return true;
        }
        return false;
    }

}
