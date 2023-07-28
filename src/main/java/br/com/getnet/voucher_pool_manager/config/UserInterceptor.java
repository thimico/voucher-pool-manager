package br.com.getnet.voucher_pool_manager.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
public class UserInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Buscar o valor do cabeçalho "Authorization"
        String authorization = request.getHeader("Authorization");

        // Verificar se o cabeçalho está ausente ou se não começa com "Bearer "
        if (Objects.isNull(authorization) || !authorization.startsWith("Bearer ")) {
            // Se o cabeçalho estiver ausente ou for inválido, definir o status da resposta como 401 (Não Autorizado)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // E interromper o processamento do pedido, retornando false
            return false;
        }

        // Se o cabeçalho estiver presente e for válido, permitir o processamento do pedido, retornando true
        return true;
    }


}