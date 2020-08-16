package com.online.university.hrynyk.repository;

import com.online.university.hrynyk.model.Position;
import com.online.university.hrynyk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByPosition(Position position);
    User findByActivationCode(String code);

}
