package ru.semen.springcourse.FirstSecurityApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.semen.springcourse.FirstSecurityApp.models.Person;
import ru.semen.springcourse.FirstSecurityApp.repositories.PeopleRepository;

@Service
public class RegistrationServise {

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public RegistrationServise(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person person) {
//        String encodedPassword = passwordEncoder.encode(person.getPassword());
//        person.setPassword(encodedPassword);// либо короче
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        peopleRepository.save(person);
    }
}
