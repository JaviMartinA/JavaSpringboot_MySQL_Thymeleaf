package com.example.hitoindividual2finalaadd.seguridad;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity {
    @Autowired
    private DataSource origenDatos;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(origenDatos)
                .usersByUsernameQuery("select nif, pw, activo from usuario where nif=?")
                .authoritiesByUsernameQuery("SELECT NIF, rol FROM roles WHERE NIF=?");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Filtros por URL.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeHttpRequests()
                // No se requiere autenticación para acceso a la raiz y al login
                .requestMatchers("/", "/login").permitAll()
                // Solo puede acceder el ADMINISTRADOR
                .requestMatchers("/admin/**").hasAuthority("ADMINISTRADOR")
                // Solo puede acceder el USUARIO
                .requestMatchers("/user/**").hasAnyAuthority("USUARIO", "ADMINISTRADOR")
                // Se requiere autenticación para el resto de rutas.
                .anyRequest().authenticated()
                // Se permite iniciar y cerrar sesión.
                .and().formLogin().loginPage("/login").permitAll()
                .and().logout().permitAll()
                // Error permiso denegado
                .and().exceptionHandling().accessDeniedPage("/denegado");
        return http.build();
    }

}
