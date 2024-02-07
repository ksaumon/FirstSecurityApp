package ru.semen.springcourse.FirstSecurityApp.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.semen.springcourse.FirstSecurityApp.dto.AuthenticationDTO;
import ru.semen.springcourse.FirstSecurityApp.dto.PersonDTO;
import ru.semen.springcourse.FirstSecurityApp.models.Person;
import ru.semen.springcourse.FirstSecurityApp.security.JWTUtil;
import ru.semen.springcourse.FirstSecurityApp.services.RegistrationServise;
import ru.semen.springcourse.FirstSecurityApp.util.PersonValidator;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PersonValidator personValidator;
    private final RegistrationServise registrationServise;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public AuthController(PersonValidator personValidator, RegistrationServise registrationServise,
                          JWTUtil jwtUtil, ModelMapper modelMapper,
                          AuthenticationManager authenticationManager) {
        this.personValidator = personValidator;
        this.registrationServise = registrationServise;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        return "auth/registration";
    }
    @PostMapping("/registration")
    public Map<String, String> performRegistration (@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {
        Person person = convertToPerson(personDTO);
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return Map.of("message", "ошибка");
        }
        registrationServise.register(person);//регистрация человека
        String token = jwtUtil.generateToken(person.getUsername());
        return Map.of("jwt-token", token);
    }

    @PostMapping("/login")//заново получаем токен при регистрации
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials!");
        }

        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        return Map.of("jwt-token", token);
    }

    public Person convertToPerson(@Valid PersonDTO personDTO) {
        return this.modelMapper.map(personDTO, Person.class);
    }
}
