package com.ict.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/** 白名单
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/5/19 0019 14:11
 */
@Component
public class FilterWhite {
    private static final Logger logger = LoggerFactory.getLogger(FilterWhite.class);

    @Autowired
    private IctConfig ictConfig;

    private HashMap<String, String> ictWhiteUrlList = new HashMap<>();

    private boolean flag = true;

    public boolean isRelease(String url) {
        if (flag) {
            synchronized (FilterWhite.class) {
                if (flag) {
                    List<String> whiteUrlList = ictConfig.getWhiteUrls();
                    if (whiteUrlList != null) {
                        for (String whiteUrl : whiteUrlList) {
                            ictWhiteUrlList.put(whiteUrl, "");
                            logger.info("filter whitelist:{}.", whiteUrl);
                        }
                        flag = false;
                    } else {
                        logger.info("filter whitelist is null.");
                    }
                }
            }
        }

        return ictWhiteUrlList.containsKey(url);
    }
}
