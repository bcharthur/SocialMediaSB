package dev.bcharthur.socialmediaspringboot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class ConfigSecurity {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(HttpMethod.GET, "/").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/index").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/integration").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/navbar/**").permitAll();
            auth.requestMatchers(HttpMethod.POST, "/navbar/**").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/navbar").permitAll();
            auth.requestMatchers(HttpMethod.POST, "/navbar").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/login/**").permitAll();
            auth.requestMatchers(HttpMethod.POST, "/login/**").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/register/**").permitAll();
            auth.requestMatchers(HttpMethod.POST, "/register/**").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/auth/**").permitAll();
            auth.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();

            auth.requestMatchers(HttpMethod.GET, "/utilisateurs/{pseudo}/detail").permitAll();


            // Allow viewing posts without authentication
            auth.requestMatchers(HttpMethod.GET, "/post/**").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll();

            // Restrict actions to authenticated users only
            auth.requestMatchers(HttpMethod.POST, "/api/posts/send").authenticated();
            auth.requestMatchers(HttpMethod.POST, "/api/posts/update/**").authenticated();
            auth.requestMatchers(HttpMethod.POST, "/api/posts/delete/**").authenticated();
            auth.requestMatchers(HttpMethod.POST, "/api/posts/hide/**").authenticated();
            auth.requestMatchers(HttpMethod.POST, "/api/posts/like/**").authenticated();
            auth.requestMatchers(HttpMethod.POST, "/api/posts/unlike/**").authenticated();
            auth.requestMatchers(HttpMethod.POST, "/api/posts/comment/**").authenticated();

            auth.requestMatchers(HttpMethod.GET, "/chat/**").authenticated();
            auth.requestMatchers(HttpMethod.POST, "/chat/**").authenticated();
            auth.requestMatchers(HttpMethod.GET, "/api/chats/**").authenticated();
            auth.requestMatchers(HttpMethod.POST, "/api/chats/**").authenticated();

            auth.requestMatchers("/css/**").permitAll()
                    .requestMatchers("/images/**").permitAll()
                    .requestMatchers("/javascript/**").permitAll()
                    .requestMatchers("/error").permitAll()
                    .requestMatchers("/upload/**").permitAll()  // Ajouté pour permettre l'accès aux images uploadées
                    .anyRequest().authenticated();
        });

        http.formLogin(form -> {
            form.loginPage("/login").permitAll();
            form.defaultSuccessUrl("/").permitAll();
            form.failureUrl("/login-error");

            form.successHandler((request, response, authentication) -> {
                if (authentication != null) {
                    MyUserDetail userDetails = (MyUserDetail) authentication.getPrincipal();
                    request.getSession().setAttribute("currentUser", userDetails.getUser());
                }
                response.sendRedirect("/");
            });
        });

        http.logout(logout -> logout.invalidateHttpSession(true).clearAuthentication(true).deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").permitAll());

        // Ajouter cette ligne pour ignorer la désérialisation des sessions précédemment enregistrées
        // http.sessionManagement(session -> session.invalidSessionStrategy((request, response) -> response.sendRedirect("/login")));

        return http.build();
    }
}
