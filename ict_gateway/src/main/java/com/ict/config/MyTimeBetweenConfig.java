package com.ict.config;

import java.time.LocalTime;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/2/24
 * @Version 1.0
 */
public class MyTimeBetweenConfig {
    private LocalTime startTime;

    private LocalTime endTime;

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}