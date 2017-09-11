package com.yk.example.controller;

import com.yk.example.entity.User;
import com.yk.example.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletException;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/8.
 */
@ApiIgnore
@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public User registerUser(@RequestBody User user) {
        return userService.save(user);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody User login) throws ServletException {

        String jwtToken = "";

        if (login.getEmail() == null || login.getPassword() == null) {
            throw new ServletException("Please fill in username and password");
        }

        String email = login.getEmail();
        String password = login.getPassword();

        User user = userService.findByEmail(email);

        if (user == null) {
            throw new ServletException("User email not found.");
        }

        String pwd = user.getPassword();

        if (!password.equals(pwd)) {
            throw new ServletException("Invalid login. Please check your name and password.");
        }

        Date date = new Date();
        DateTime dateTime = new DateTime(date);
        Date exprireDate = dateTime.plusMinutes(1).toDate();
        jwtToken = Jwts.builder().setSubject(email).claim("roles", "user").setIssuedAt(date).setExpiration(exprireDate)
                .signWith(SignatureAlgorithm.HS256, "secretkey").compact();

        return jwtToken;
    }
}
