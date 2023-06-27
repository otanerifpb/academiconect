package br.edu.ifpb.pweb2.academiConect.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class AcademicSecurityConfig extends WebSecurityConfigurerAdapter{
    
    @Autowired
    DataSource dataSource;

    // REQFUNC 13 - Autenticação e Autorização
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // Permite o acesso sem autenticação
                .antMatchers("/css/**", "/imagens/**")
                // Tudo acessível para quem estar em css e imagens
                .permitAll()
                // Toda requisição da Url
                .anyRequest()
                // O usuário autenticado pode acessar tudo
                .authenticated()
                .and()
                // Formulário para a recolha dos dados de usuário e senha
                .formLogin(form -> form
                    // Rota para a página de Login definida
                    .loginPage("/auth")
                    // Rota para o caso do sucesso do Login, chama página
                    .defaultSuccessUrl("/home", true)
                    // Permite tudo para o usuário que estive
                    .permitAll())  /**/
                .logout(logout -> logout.logoutUrl("/auth/logout"));
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        auth.jdbcAuthentication()
            .dataSource(dataSource)
            // Criptografia da senha do usuário, lembrar de tirar ";" quando for rodar em um banco novo 
            .passwordEncoder(encoder);
            // Os métodos withUser(), só precisa ser executado 1 única vez, depois é necessário comentar
            // Caso seja executado mais de uma vez, ocorrerá um erro de duplicidade de PK no banco
            // Mudar os ";" para passwordEncoder(encoder), depois de comentar user abaixo
            //.withUser(User.builder().username("admin").password(encoder.encode("useradmin")).roles("USER", "ADMIN").build())
            //.withUser(User.builder().username("visitante").password(encoder.encode("uservis")).roles("USER").build())
            //.withUser(User.builder().username("teste").password(encoder.encode("usertes")).roles("USER").build());
    }
}
