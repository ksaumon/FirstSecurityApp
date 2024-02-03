package ru.semen.springcourse.FirstSecurityApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import ru.semen.springcourse.FirstSecurityApp.security.AuthProviderImpl;
import ru.semen.springcourse.FirstSecurityApp.services.PersonDetailsService;


@EnableWebSecurity
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
        http.csrf().disable()//отключаем защиту от межсайтовой потделки токинов
                .authorizeRequests()
                .antMatchers("/auth/login", "/auth/registration","/error").permitAll()// на эти адреса/auth/login и /error-пускаем всех
                .anyRequest().authenticated()// на остальные страници не пускаем не аунтифицированных пользователей
                .and()//переходим к страничке логина
                .formLogin().loginPage("/auth/login")//настраиваем форму для логина
                .loginProcessingUrl("/process_login")//по этому адресу спринг ждет данные формы
                .defaultSuccessUrl("/hello", true)//указываем что будет происходить после
                // успешной аунтификации(true означает что всегда перенапрявлять по адресу /hello)
                .failureUrl("/auth/login?error");//если аунтификации не успешно перевод на адрес
                //  /auth/login?error
    }

    // Настраивает аутентификацию
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }//показываем спрингу что пороль не шифруеться
}
