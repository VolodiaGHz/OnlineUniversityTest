package com.online.university.hrynyk.service;

import com.online.university.hrynyk.model.Lesson;
import com.online.university.hrynyk.model.Position;
import com.online.university.hrynyk.model.Subject;
import com.online.university.hrynyk.model.User;
import com.online.university.hrynyk.repository.LessonRepo;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

@Service
public class LogicService {
    public static final String DATE_REGEX = "00-00-00";
    private final LessonRepo lessonRepo;

    public LogicService(LessonRepo lessonRepo) {
        this.lessonRepo = lessonRepo;
    }

    public Subject findSubjectField(String position) {
        Set<Subject> subjects = new HashSet<>(Arrays.asList(Subject.values()));
        for (Subject subject : subjects) {
            int count = 0;
            for (int i = 0; i < subject.name().length(); i++) {
                if (subject.name().length() - 1 > i && position.length() - 1 > i) {
                    if (subject.name().charAt(i) == position.charAt(i)) {
                        count++;
                    }
                }
            }
            if (count >= subject.name().length() - 1) {
                return subject;
            }
        }
        return null;
    }

    public Position findPosition(String subject) {
        Set<Position> positions = new HashSet<>(Arrays.asList(Position.values()));
        for (Position position : positions) {
            int count = 0;
            for (int i = 0; i < subject.length(); i++) {
                if (subject.length() - 1 > i && position.name().length() - 1 > i) {
                    if (subject.charAt(i) == position.name().charAt(i)) {
                        count++;
                    }
                }
            }
            if (count >= subject.length() - 1) {
                return position;
            }
        }
        return null;
    }

    public boolean ifTimeSlotExists(User user, Date date, Time time) {
        Lesson lesson = lessonRepo.findByDateAndTime(date, time);
        if (lesson != null && user != null && lesson.getTeacher_id().getId().equals(user.getId())) {
            if (!lesson.getStatus().equals("decline") && !lesson.getStatus().equals("none")) {
                return true;
            }
        }
        return false;
    }

    public boolean ifTimeSlotAvailable(Date date, Time time, User user) {
        List<Lesson> lessonList = lessonRepo.findByDate(date);
        if (lessonList.size() > 0) {
            for (Lesson lesson : lessonList) {
                if (lesson.getTeacher_id() != null && lesson.getTeacher_id().getId().equals(user.getId())) {
                    if (!lesson.getStatus().equals("decline") && !lesson.getStatus().equals("none")) {
                        String newTime = transformTime(lesson.getTime().toString(),
                                lesson.getPrice_id().getDuration(), lesson.getDate());
                        String[] splitTime = newTime.split("/");
                        String newLessonDate = splitTime[0];
                        String fullTimeOfLesson = splitTime[1];
                        LocalTime lessonFullTime = LocalTime.parse(fullTimeOfLesson);
                        LocalTime newLessonTime = LocalTime.parse(time.toString());
                        if (newLessonDate.equals(DATE_REGEX)) {
                            if (lessonFullTime.isAfter(newLessonTime) || lessonFullTime.equals(newLessonTime)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public String transformTime(String time, String duration, Date date) {
        int timeHours = Integer.parseInt(time.substring(0, 2));
        int timeMinutes = Integer.parseInt(time.substring(3, 5));
        if (duration.substring(0, 2).equals("1:")) {
            duration = "0" + duration;
        }
        int durationHours = Integer.parseInt(duration.substring(0, 2));
        int durationMinutes = Integer.parseInt(duration.substring(3, 5));
        int fullHours = timeHours + durationHours;
        int fullMinutes = timeMinutes + durationMinutes;
        if (fullMinutes > 60) {
            fullMinutes = fullMinutes - 60;
            fullHours = fullHours + 1;
        } else if (fullHours > 23) {
            fullHours = fullHours - 23;
            String fullTime = fullHours + ":" + fullMinutes + ":00";
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);
            return calendar.toString() + "/" + fullTime;
        }
        if (fullMinutes < 10) {
            String fullTime = fullHours + ":" + "0" + fullMinutes + ":00";
            return DATE_REGEX + "/" + fullTime;
        }
        String fullTime = fullHours + ":" + fullMinutes + ":00";
        return DATE_REGEX + "/" + fullTime;

    }
}
