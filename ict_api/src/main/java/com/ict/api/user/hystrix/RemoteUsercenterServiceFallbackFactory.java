package com.ict.api.user.hystrix;

import com.ict.api.user.RemoteUsercenterService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 降级处理
 * @author wsr
 */
@Component
public class RemoteUsercenterServiceFallbackFactory implements FallbackFactory<RemoteUsercenterService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteUsercenterServiceFallbackFactory.class);

    @Override
    public RemoteUsercenterService create(Throwable throwable) {
        return new RemoteUsercenterService() {

            @Override
            public Map<String, Object> getUserInfo(String userId) throws Exception {
                Map<String, Object> map = new HashMap<>();
                map.put("msg", "服务器繁忙，请稍后重试");
                return map;
            }
        };
    }
}
