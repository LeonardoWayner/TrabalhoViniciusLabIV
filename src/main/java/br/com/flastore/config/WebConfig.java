package br.com.flastore.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                var session = request.getSession(false);
                if (session != null && Boolean.TRUE.equals(session.getAttribute("logado"))) return true;
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }).addPathPatterns("/**").excludePathPatterns("/login", "/css/**", "/js/**", "/img/**", "/error", "/favicon.ico");
    }
}
