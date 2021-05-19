package com.ict.filter;

import com.ict.config.FilterWhite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
import java.util.List;

/** 自定义全局过滤器
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/5/19 0019 13:54
 */
@Component
public class GlobelGatewayFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(GlobelGatewayFilter.class);
    private final static String REQUEST_URL_EMT = "#";

    @Autowired
    private FilterWhite whiteList;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //获取请求的url
        String requestUrl = request.getURI().getPath();

        if (REQUEST_URL_EMT.equals(requestUrl)) {
            logger.info("请求路径错误,return null.");
            return responseResult(response, "200001");
        }

        if (whiteList.isRelease(requestUrl)) {
            logger.info("URL:{} 方法在白名单中无需token验证...", requestUrl);
            return chain.filter(exchange);
        }

        // 校验token
//        String token = getToken(request);
//        if (token == null || "".equals(token)) {
//            logger.info("请求无 token URL:{}.", requestUrl);
//            return responseResult(response, ResultCode.TOKEN_ERR);
//        }
//
//        String json = this.redis.get(token);
//        if (json == null || "".equals(json)) {
//            logger.info("Token:[{}], token无效或过期 URL:{}.", token, requestUrl);
//            return responseResult(response, ResultCode.TOKEN_ERR);
//        }

        // 校验用户菜单权限
//        User userInfo = null;
//        List<TMenu> menuList = new LinkedList<>();
//        try {
//            userInfo = (UserInfo) JSONUtil.toBean(json, UserInfo.class);
//            if (userInfo != null) {
//                menuList = userInfo.getMenuList();
//            }
//            if (menuList == null) {
//                logger.info("Token:[{}], 该用户无对应菜单权限.", token);
//                return responseResult(response, ResultCode.TOKEN_NO_MENU);
//            }
//        } catch (Exception e) {
//            logger.error("Token:[{}], token获取用户信息失败 :{} parse error.", token, json);
//            return responseResult(response, ResultCode.ERROR);
//        }

//        boolean authorityFlag = true;
//        for (TMenu menu : menuList) {
//            if (requestUrl.startsWith(menu.getBackPath())) {
//                authorityFlag = false;
//                break;
//            }
//        }
//        if (authorityFlag) {
//            logger.info("Token:[{}], 用户没有权限 URL:{}.", token, requestUrl);
//            return responseResult(response, ResultCode.AUTH_FAIL);
//        }
//        logger.info("Token:[{}], 鉴权成功,访问路径及token打印 URL:{}.", token, requestUrl);
        // 放行
        return chain.filter(exchange);
    }

    /**
     * 封装返回失败请求值
     * @param response
     * @param code
     * @return
     */
    private Mono<Void> responseResult(ServerHttpResponse response, String code) {
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        DataBufferFactory bufferFactory = response.bufferFactory();
        byte[] bytes = code.getBytes();
        DataBuffer buffer = bufferFactory.wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        // 过滤器加载的顺序 值越小优先级越高
        return 0;
    }
}
