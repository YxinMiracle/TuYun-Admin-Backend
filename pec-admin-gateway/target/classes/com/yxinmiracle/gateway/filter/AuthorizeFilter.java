package com.yxinmiracle.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.yxinmiracle.gateway.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    Logger logger = LoggerFactory.getLogger(AuthorizeFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取请求对象和响应对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //2.判断当前的请求是否为登录，如果是，直接放行
        if (request.getURI().getPath().contains("/api/v1/user/login") || request.getURI().getPath().contains("/file/uploadchunk") || request.getURI().getPath().contains("/api/v1/file/test")) {
            //放行
            return chain.filter(exchange);
        }
        //3.获取当前用户的请求头jwt信息
        HttpHeaders headers = request.getHeaders();
        String jwtToken = headers.getFirst("token");
        //4.判断当前令牌是否存在
        if (StringUtils.isEmpty(jwtToken)) {
            //如果不存在，向客户端返回错误提示信息
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        try {
            //5.如果令牌存在，解析jwt令牌，判断该令牌是否合法，如果不合法，则向客户端返回错误信息
            Map<String, Claim> stringClaimMap = JwtUtil.verifyToken(jwtToken);
            Integer userId = JwtUtil.getAppUID(jwtToken);
            //5.1 合法，则向header中重新设置userId
//            int userId = Integer.valueOf((Integer) claims.get("userId"));
            logger.info("find userId:{} from uri:{}", userId, request.getURI());
            //重新设置token到header中
            ServerHttpRequest serverHttpRequest = request.mutate().headers(httpHeaders -> {
                httpHeaders.add("userId", userId + "");
                httpHeaders.add("token", jwtToken);
            }).build();
            exchange.mutate().request(serverHttpRequest).build();
        } catch (Exception e) { // token无效
            e.printStackTrace();
            logger.error("token无效");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //6.放行
        return chain.filter(exchange);
    }

    /**
     * 优先级设置
     * 值越小，优先级越高
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
