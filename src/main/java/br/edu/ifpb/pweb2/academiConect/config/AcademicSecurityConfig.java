package br.edu.ifpb.pweb2.academiConect.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity /*Habilita o acesso se autorizado */
//@EnableGlobalMethodSecurity(prePostEnabled = true) /*Habilita o acesso se autorizado */
public class AcademicSecurityConfig extends WebSecurityConfigurerAdapter{
    
    @Autowired
    DataSource dataSource;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    // REQFUNC 13 - Autenticação e Autorização
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() 
                .antMatchers("/css/**", "/imagens/**")  
                .permitAll()
                .antMatchers("/estudantes/**").hasRole("ADMIN")
                .antMatchers("/declaracoes/**").hasRole("ADMIN")
                .antMatchers("/login/**").hasRole("ADMIN")
                .antMatchers("/documento/**").hasRole("ADMIN")
                .antMatchers("/instituicoes/**").hasAnyRole("VIS","USER", "ADMIN")
                .antMatchers("/instituicoes/listInst").hasAnyRole("VIS","USER", "ADMIN")
                .antMatchers("/periodos/**").hasAnyRole("VIS","USER", "ADMIN")
                .antMatchers("/periodos/listPeri").hasAnyRole("VIS","USER", "ADMIN")  
                .anyRequest()  
                .authenticated() 
                .and()
                .formLogin(form -> form 
                    .loginPage("/auth")  
                    .defaultSuccessUrl("/home", true) 
                    .permitAll())  
                .logout(logout -> logout.logoutUrl("/auth/logout"))
                //.and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);
    }
    /*.antMatchers() - Permite o acesso sem autenticação*/
    /*.permitAll() - Tudo acessível para quem estar em css e imagens*/
    /*.anyRequest() - Toda requisição da Url */
    /*.authenticated() - O usuário autenticado pode acessar tudo */
    /*.formLogin() - Formulário para a recolha dos dados de usuário e senha */
    /*.loginPage() - Rota para a página de Login para entrar no sistema*/
    /*.defaultSuccessUrl() - Rota para o caso do sucesso do Login, chama página */
    /*.permitAll() - Permite tudo para o usuário que estive */
    /*.logout() - Rota para a página de Logout para sair do sistema */
    /*.hasRole() - Permite o acesso para 1 unico Perfil de usuário */
    /*.hasAnyRole() - Permite o acesso para mais de 1 Perfil de usuário */
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        auth.jdbcAuthentication()
            .dataSource(dataSource)
            // Criptografia da senha do usuário, lembrar de tirar ";" quando for rodar em um banco novo 
            .passwordEncoder(encoder); /*; */
            // .withUser(User.builder().username("admin").password(encoder.encode("useradm")).roles("USER","ADMIN").build())
            // .withUser(User.builder().username("estudante").password(encoder.encode("userest")).roles("USER").build())
            // .withUser(User.builder().username("visitante").password(encoder.encode("uservis")).roles("VIS").build());

            // Os métodos withUser(), só precisa ser executado 1 única vez, depois é necessário comentar
            // Caso seja executado mais de uma vez, ocorrerá um erro de duplicidade de PK no banco
            // Mudar os ";" para passwordEncoder(encoder), depois de comentar user abaixo
            // Caso seja necessário criar novamente os perfis acima, devem ser criados 1 por vez, se for 3 da error
    }
}
