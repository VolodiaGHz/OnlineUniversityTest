package com.online.university.hrynyk.repository;

import com.online.university.hrynyk.model.Lesson;
import com.online.university.hrynyk.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface LessonRepo extends JpaRepository<Lesson, Long> {
    Lesson findByDateAndTime(Date date, Time time);

    List<Lesson> findByDate(Date date);

    List<Lesson> findBySubject(Subject subject);
}
