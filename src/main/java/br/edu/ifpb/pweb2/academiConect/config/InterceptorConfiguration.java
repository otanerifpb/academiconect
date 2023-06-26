package br.edu.ifpb.pweb2.academiConect.config;

import org.apache.tomcat.jni.Registry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.edu.ifpb.pweb2.academiConect.interceptor.AuthInterceptor;

// REQFUNC 13 - Autenticação e Autorização
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer{

    @Autowired
    AuthInterceptor authInterceptor;

    // Rota para iniciar a configuração do Interceptor
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                // Chama durante a inicialização da configuração
                .addInterceptor(authInterceptor)
                // Indicar quais recursos necessita ter autirização para ter acesso
                .addPathPatterns("/**", "/estudantes", "/declaracoes", 
                    "/instituicoes", "/periodos" )
                // Indicar quais recursos não necessita ter autirização para acessar
                .excludePathPatterns("/auth/**", "/css/**", "/imagens/**");
    }
    
}
