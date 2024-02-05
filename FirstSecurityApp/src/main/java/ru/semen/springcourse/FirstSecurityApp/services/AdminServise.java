package ru.semen.springcourse.FirstSecurityApp.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AdminServise {
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SOME_OTHER')")//доступ ролей к методу doAdminStuff()
    public void doAdminStuff() {
        System.out.println("Здесь только администратор");
    }
}
