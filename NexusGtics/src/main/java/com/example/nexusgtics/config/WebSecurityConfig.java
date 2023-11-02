package com.example.nexusgtics.config;

import com.example.nexusgtics.repository.TicketRepository;
import com.example.nexusgtics.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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

    final UsuarioRepository usuarioRepository;
    final TicketRepository ticketRepository;
    final DataSource dataSource;

    public WebSecurityConfig(DataSource dataSource, UsuarioRepository usuarioRepository, TicketRepository ticketRepository) {
        this.dataSource = dataSource;
        this.usuarioRepository = usuarioRepository;
        this.ticketRepository = ticketRepository;
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

                    HttpSession session = request.getSession();
                    session.setAttribute("usuario", usuarioRepository.findByCorreo(authentication.getName()));

                    if(defaultSavedRequest != null){
                        String targetURl = defaultSavedRequest.getRequestURL();
                        new DefaultRedirectStrategy().sendRedirect(request,response,targetURl);
                    }else{
                        String rol = "";
                        for(GrantedAuthority role : authentication.getAuthorities()){
                            rol = role.getAuthority();
                            break;
                        }
                        System.out.println(rol);
                        switch (rol) {
                            case "Super administrador" -> response.sendRedirect(request.getContextPath()+"/superadmin");
                            case "Administrador" -> response.sendRedirect(request.getContextPath()+"/admin");
                            case "Analista de OyM" -> response.sendRedirect(request.getContextPath()+"/analistaOYM");
                            case "Analista de Planificacion o Ingenieria" ->
                                    response.sendRedirect(request.getContextPath()+"/analistaDespliegue");
                            case "Supervisor de Campo" -> response.sendRedirect(request.getContextPath()+"/supervisor");
                            case "Tecnico" -> {
                                session.setAttribute("listaTicketSession", ticketRepository.findAll());
                                response.sendRedirect(request.getContextPath() + "/tecnico");
                            }
                            default -> response.sendRedirect(request.getContextPath()+"/login");
                        }
                        System.out.println(rol);
                    }
                }
                );

        http.authorizeHttpRequests()
                .requestMatchers("/superadmin", "/superadmin/**").hasAnyAuthority("Super administrador")
                .requestMatchers("/admin", "/admin/**").hasAnyAuthority("Administrador")
                .requestMatchers("/analistaOYM", "/analistaOYM/**").hasAnyAuthority("Analista de OyM")
                .requestMatchers("/analistaDespliegue", "/analistaDespliegue/**").hasAnyAuthority("Analista de Planificacion o Ingenieria")
                .requestMatchers("/supervisor", "/supervisor/**").hasAnyAuthority("Supervisor de Campo")
                .requestMatchers("/tecnico", "/tecnico/**").hasAnyAuthority("Tecnico")
                .anyRequest().permitAll();
        http.logout()
                .logoutSuccessUrl("/closeSession")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);
                System.out.println("Cerro sesion");
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
