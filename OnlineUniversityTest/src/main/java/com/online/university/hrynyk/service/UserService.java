package com.online.university.hrynyk.service;

import com.online.university.hrynyk.model.Position;
import com.online.university.hrynyk.model.Role;
import com.online.university.hrynyk.model.User;
import com.online.university.hrynyk.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final MailSender mailSender;
    public UserService(UserRepo userRepo, MailSender mailSender) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo.findByEmail(email);
    }

    public boolean registrationUser(User user, Role role, Position position) {
        User userEmail = userRepo.findByEmail(user.getEmail());
        if (userEmail != null) {
            return false;
        }
        user.setActive(true);
        if (role.equals(Role.STUDENT)) {
            user.setRoles(Collections.singleton(role));
            user.setPosition(Collections.singleton(position));

        } else if (role.equals(Role.TEACHER)) {
            user.setRoles(Collections.singleton(role));
            user.setPosition(Collections.singleton(position));
        }
        if (!StringUtils.isEmpty(user.getEmail())){
            String message = String.format(
                    "Hello "+ user.getFullName() + "\n" +
                            "Welcome to online University. Please visit this link http://localhost:8080/",
                    user.getUsername()
            );
            mailSender.sendMail(user.getEmail(), "Thanks for registration", message);
        }
        userRepo.save(user);
        return true;
    }


}

