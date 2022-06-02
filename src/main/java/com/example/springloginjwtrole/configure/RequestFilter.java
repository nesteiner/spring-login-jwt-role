package com.example.springloginjwtrole.configure;

import com.example.springloginjwtrole.utils.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class RequestFilter extends OncePerRequestFilter {
    @Value("${jwt.header.string}")
    public String HEADER_STRING;

    @Value("${jwt.token.prefix}")
    public String TOKEN_PREFIX;

    @Autowired
    TokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setContentType("application/json; charset=utf-8");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        String header = request.getHeader(HEADER_STRING);
        String username = null, authenticationToken = null;

        if(header != null && header.startsWith(TOKEN_PREFIX)) {
            authenticationToken = header.replace(TOKEN_PREFIX, "");
            try {
                username = jwtTokenUtil.getUsernameFromToken(authenticationToken);
                request.setAttribute("username", username);
                request.setAttribute("authenticationToken", authenticationToken);
                filterChain.doFilter(request, response);
            } catch (IllegalArgumentException e) {
                node.put("status", "access failed");
                node.put("message", "An error occurred while fetching Username from Token");
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
                response.getWriter().write(json);
                return;
            } catch (ExpiredJwtException e) {
                node.put("status", "access failed");
                node.put("message", "jwt token has been expired");
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
                response.getWriter().write(json);
                return;
            } catch (SignatureException e) {
                node.put("status", "access failed");
                node.put("message", "authentication failed. username or password not valid");
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
                response.getWriter().write(json);
                return;
            }
        } else if(header != null) {
            log.warn("couln't find bearer string, header will be ignored");

            node.put("status", "access failed");
            node.put("message", "jwt token parse error");
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
            response.getWriter().write(json);
            return;
        } else if(header == null) {
            // in this case, request goto login
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(request, response);
        }


    }
}
