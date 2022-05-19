package com.inst.testprogectinst.security.jwt;

import com.google.gson.Gson;
import com.inst.testprogectinst.payload.response.InvalidLoginResponse;
import com.inst.testprogectinst.security.SecurityConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    private final InvalidLoginResponse invalidLoginResponse;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String jsonLoginResp = new Gson().toJson(invalidLoginResponse);
        response.setContentType(SecurityConst.CONTENT_TYPE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().println(jsonLoginResp);
    }
}
