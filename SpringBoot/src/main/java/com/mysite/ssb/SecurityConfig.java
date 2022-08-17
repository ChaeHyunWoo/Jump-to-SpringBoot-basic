package com.mysite.ssb;

import com.mysite.ssb.user.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// Spring Security 설정
@RequiredArgsConstructor
@Configuration // 스프링의 환경설정 파일임을 의미
@EnableWebSecurity // 사용하면 내부적으로 SpringSecurityFilterChain이 동작하여 URL 필터가 적용
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserSecurityService userSecurityService;

    // 스프링 시큐리티의 세부 설정은 SecurityFilterChain 빈을 생성하여 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 모든 인증되지 않응 요청을 허락한다는 의미 (하단 소스) -> 따라서 로그인을 하지 않아도 모든 페이지 접근 가능
        http.authorizeHttpRequests().antMatchers("/**").permitAll()
                .and() // 스프링 시큐리티가 CSRF 처리시 H2 콘솔은 예외로 처리할 수 있도록 설정
                    .csrf().ignoringAntMatchers("/h2-console/**")
                .and()
                    .headers()
                    .addHeaderWriter(new XFrameOptionsHeaderWriter(
                            XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                // URL 요청시 X-Frame-Options 헤더값을 sameorigin으로 설정하여 오류가 발생하지 않도록 함

                // and().formLogin().loginPage("/user/login").defaultSuccessUrl("/")는
                // 스프링 시큐리티의 로그인 설정을 담당하는 부분으로 로그인 페이지의 URL은 /user/login이고 로그인 성공시에
                // 이동하는 디폴트 페이지는 루트 URL(/)임을 의미
                .and()
                    .formLogin()
                    .loginPage("/user/login")
                    .defaultSuccessUrl("/")
                .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true);

        // and() - http 객체의 설정을 이어서 할 수 있게 하는 메서드이다.
        // http.csrf().ignoringAntMatchers("/h2-console/**") - /h2-console/로 시작하는 URL은 CSRF 검증을 하지 않는다는 설정이다.
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // AuthenticationManager는 스프링 시큐리티의 인증을 담당
    // - AuthenticationManager 빈 생성시 스프링의 내부 동작으로 인해 위에서 작성한 UserSecurityService와 PasswordEncoder가 자동으로 설정
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
        throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }
}
