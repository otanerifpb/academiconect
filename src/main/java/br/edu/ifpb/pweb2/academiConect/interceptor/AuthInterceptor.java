package br.edu.ifpb.pweb2.academiConect.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.pweb2.academiConect.model.Estudante;

@Component
public class AuthInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        boolean allowed = false;
        HttpSession httpSession = request.getSession(false);

        if(httpSession != null && ((Estudante) httpSession.getAttribute("usuario")) != null) {
            // verifica se tem um usuário guardado na sessão, caso afirmatido, solicitar Login!
            allowed = true;
        } else {
            // Nome da aplicação
            String baseUrl = request.getContextPath();
            // Para o caso de não ter sessão ou ela esta vazia, redireciona para auth/formLogin.html
            String paginaLogin = baseUrl + "/auth/formLogin";
            response.sendRedirect(response.encodeRedirectURL(paginaLogin));
            allowed = false;
        }
        return allowed;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Sem uso
    }

    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
        // Sem uso
    }
}
