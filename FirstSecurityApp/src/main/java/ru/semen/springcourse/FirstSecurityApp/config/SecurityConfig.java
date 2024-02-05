package ru.semen.springcourse.FirstSecurityApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import ru.semen.springcourse.FirstSecurityApp.security.AuthProviderImpl;
import ru.semen.springcourse.FirstSecurityApp.services.PersonDetailsService;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)// анатация указывающея спринг сикьюрети на ограничение ролелям доступ
//с помощью навешивание анотации @PreAuthorize в сервисе
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    private final AuthProviderImpl authProvider;
//
//    @Autowired
//    public SecurityConfig(AuthProviderImpl authProvider) {
//        this.authProvider = authProvider;
//    }

    // Настраивает аутентификацию
//    protected void configure(AuthenticationManagerBuilder auth) {
//        auth.authenticationProvider(authProvider);
//    }
    //вместо того что бы внедрять AuthProviderImpl мы внедряем сразу PersonDetailsService

    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    protected void configure(HttpSecurity http) throws Exception {
        // конфигурируем Spring Security(какая страница отвечает за вход, какая за ошибки и.т.д)
        // конфигурируем авторизацию(дать доступ пользователю на основание его статуса к страцицам)
        http.authorizeRequests()
                //.antMatchers("/admin").hasRole("ADMIN")//для запроса на строницу /admin имеет достут полько ADMIN
                .antMatchers("/auth/login", "/auth/registration","/error").permitAll()// на эти адреса/auth/login
                // и /error-пускаем всех
                .anyRequest().hasAnyRole("USER", "ADMIN")//для всех запросо имеют доступ "USER", "ADMIN"
                .and()//переходим к страничке логина
                .formLogin().loginPage("/auth/login")//настраиваем форму для логина
                .loginProcessingUrl("/process_login")//по этому адресу спринг ждет данные формы
                .defaultSuccessUrl("/hello", true)//указываем что будет происходить после
                // успешной аунтификации(true означает что всегда перенапрявлять по адресу /hello)
                .failureUrl("/auth/login?error")//если аунтификации не успешно перевод на адрес
                //  /auth/login?error
                .and()
                .logout()// логаут это удаление пользователя из сесии и удаление куки из браузера
                .logoutUrl("/logout")//url по переходе по которому будет производиться логаут
                .logoutSuccessUrl("/auth/login");//url на который переходит пользователь при успешном логауте

    }

    // Настраивает аутентификацию
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }//шифрование через бикрипт
}
