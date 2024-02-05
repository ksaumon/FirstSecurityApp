package ru.semen.springcourse.FirstSecurityApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.semen.springcourse.FirstSecurityApp.security.PersonDetails;
import ru.semen.springcourse.FirstSecurityApp.services.AdminServise;

@Controller
public class HelloController {
    private final AdminServise adminServise;

    @Autowired
    public HelloController(AdminServise adminServise) {
        this.adminServise = adminServise;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "hello";
    }

    @GetMapping("/showUserInfo")
    public String showUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        System.out.println(personDetails.getPerson());
        return "hello";
    }

    @GetMapping("/admin")
    public String adminPage() {
        adminServise.doAdminStuff();
        return "admin";
    }

}
