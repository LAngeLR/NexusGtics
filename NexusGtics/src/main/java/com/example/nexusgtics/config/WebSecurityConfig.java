package com.example.nexusgtics.config;

import com.example.nexusgtics.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig {
    //final UsuarioRepository usuarioRepository;
    final DataSource dataSource;

    public WebSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
        //this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.formLogin()
                .loginPage("/openLoginWindow")
                .loginProcessingUrl("/processLogin")
                .usernameParameter("yourUsername")
                .passwordParameter("yourPassword")
                .successHandler((request, response, authentication) -> {

                    DefaultSavedRequest defaultSavedRequest =
                            (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

//                    HttpSession session = request.getSession();
//                    session.setAttribute("usuario", usuarioRepository.findByCorreo(authentication.getName()));

                    //si vengo por url -> defaultSR existe
                    if(defaultSavedRequest != null){
                        String targetURl = defaultSavedRequest.getRequestURL();
                        new DefaultRedirectStrategy().sendRedirect(request,response,targetURl);
                    }else{ //estoy viniendo del botón de login
                        String rol = "";
                        for(GrantedAuthority role : authentication.getAuthorities()){
                            rol = role.getAuthority();
                            break;
                        }

                        if(rol.equals("Super administrador")){
                            response.sendRedirect("/superadmin");

                        } else if (rol.equals("Administrador")) {
                            response.sendRedirect("/admin");

                        } else if (rol.equals("Analista de OyM")) {
                            response.sendRedirect("/analistaOYM");

                        } else if (rol.equals("Analista de Planificación o Ingeniería")) {
                            response.sendRedirect("/analistaDespliegue");

                        } else if (rol.equals("Supervisor de Campo")) {
                            response.sendRedirect("/supervisor");

                        } else if (rol.equals("Técnico")){
                            response.sendRedirect("/tecnico");

                        } else {
                            response.sendRedirect("/login");
                        }

                    }
                });

        http.authorizeHttpRequests()
                .requestMatchers("/superadmin", "/superadmin/**").hasAnyAuthority("Super administrador")
                .requestMatchers("/admin", "/admin/**").hasAnyAuthority("Administrador")
                .requestMatchers("/analistaOYM", "/analistaOYM/**").hasAnyAuthority("Analista de OyM")
                .requestMatchers("/analistaDespliegue", "/analistaDespliegue/**").hasAnyAuthority("Analista de Planificación o Ingeniería")
                .requestMatchers("/supervisor", "/supervisor/**").hasAnyAuthority("Supervisor de Campo")
                .requestMatchers("/tecnico", "/tecnico/**").hasAnyAuthority("Técnico");

        http.logout()
                .logoutSuccessUrl("/closeSession")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);


        return http.build();
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        //para loguearse sqlAuth -> username | password | enable
        String sqlAuth = "SELECT correo,contrasenia,habilitado FROM usuarios where correo = ?";

        //para autenticación -> username, nombre del rol
        String sqlAuto = "SELECT u.correo, c.nombreCargo FROM usuarios u " +
                "inner join cargos c on u.idCargos = c.idCargos " +
                "where u.correo = ?";

        users.setUsersByUsernameQuery(sqlAuth);
        users.setAuthoritiesByUsernameQuery(sqlAuto);

        return users;
    }


}
