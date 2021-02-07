package com.ict.api.user;

import com.ict.api.user.hystrix.RemoteUsercenterServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Feign调用远程
 *  ServiceNameConstants.SMS_SERVICE = "usercenter",短信服务名
 */
@FeignClient(value = "usercenter", fallbackFactory = RemoteUsercenterServiceFallbackFactory.class)
public interface RemoteUsercenterService {
    // 一定要加@RequestParam 否则调用时候会报错: 参数太长
    @GetMapping("/user/user/info")
    Map<String, Object> getUserInfo(@RequestParam("userId") String userId) throws Exception;
}
