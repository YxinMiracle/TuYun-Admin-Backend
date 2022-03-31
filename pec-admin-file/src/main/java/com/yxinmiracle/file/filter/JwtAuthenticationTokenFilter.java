package com.yxinmiracle.file.filter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.yxinmiracle.model.login.pojos.LoginUser;
import com.yxinmiracle.utils.common.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = httpServletRequest.getHeader("token");
        if (StringUtils.isBlank(token)){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }

        try {
            Map<String, Claim> stringClaimMap = JwtUtil.verifyToken(token);
            Integer userId = JwtUtil.getAppUID(token);
            String loginUserString = stringRedisTemplate.boundValueOps("pec.user_" + userId).get();
            LoginUser loginUser = JSON.parseObject(loginUserString, LoginUser.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId,null,loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token不合法");
        }
    }
}
