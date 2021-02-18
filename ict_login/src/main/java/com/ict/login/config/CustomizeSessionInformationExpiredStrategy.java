package com.ict.login.config;

import com.alibaba.fastjson.JSON;
import com.ict.login.common.JsonResult;
import com.ict.login.common.ResultCode;
import com.ict.login.common.ResultTool;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 会话信息过期策略
 * @Author wangsr
 * @Date 2021/2/18
 * @Version 1.0
 */
@Component
public class CustomizeSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
        JsonResult result = ResultTool.fail(ResultCode.USER_ACCOUNT_USE_BY_OTHERS);
        HttpServletResponse httpServletResponse = sessionInformationExpiredEvent.getResponse();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }
}