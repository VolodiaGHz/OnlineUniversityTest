package com.online.university.hrynyk.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.sql.Time;
import java.util.Set;

@Entity
@Table(name = "lesson_data")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date date;
    private Time time;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private User teacher_id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private User student_id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "price_id")
    private PriceData price_id;
    private String status;
    @ElementCollection(targetClass = Subject.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "subject_data", joinColumns = @JoinColumn(name = "lesson_id"))
    @Enumerated(EnumType.STRING)
    private Set<Subject> subject;

    public Lesson() {
    }

    public Lesson(Date date, Time time, PriceData price_id) {
        this.price_id = price_id;
        this.date = date;
        this.time = time;
    }

    public PriceData getPrice_id() {
        return price_id;
    }

    public void setPrice_id(PriceData price_id) {
        this.price_id = price_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public User getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(User teacher_id) {
        this.teacher_id = teacher_id;
    }

    public User getStudent_id() {
        return student_id;
    }

    public void setStudent_id(User student_id) {
        this.student_id = student_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Subject> getSubject() {
        return subject;
    }

    public void setSubject(Set<Subject> subject) {
        this.subject = subject;
    }

}
