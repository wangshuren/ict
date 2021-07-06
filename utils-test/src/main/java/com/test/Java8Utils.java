package com.test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

/**
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/6/16 0016 9:32
 */
public class Java8Utils {
    // Date API

    public static void main(String[] args) {
//        clock();
//        timeZones();
//        localTime();
//        localDate();
        localDateTime();
    }

    // Clock时钟
    public static void clock() {
        Clock clock = Clock.systemDefaultZone();
        long millis = clock.millis();
        long millis2 = System.currentTimeMillis();
        System.out.println(millis + " : " + millis2);

        Instant instant = clock.instant();
        Date date = Date.from(instant);
        System.out.println(date + " : " + new Date());
    }

    // TimeZones时区 时区定义了到UTS时间的时间差
    public static void timeZones() {
        System.out.println(ZoneId.getAvailableZoneIds());

        ZoneId zone1 = ZoneId.of("Europe/Berlin");
        ZoneId zone2 = ZoneId.of("Brazil/East");
        System.out.println(zone1.getRules());
        System.out.println(zone2.getRules());
        System.out.println(ZoneId.of("Asia/Shanghai").getRules());
    }

    // LocalTime 本地时间
    public static void localTime() {
        ZoneId zone1 = ZoneId.of("Europe/Berlin");
        ZoneId zone2 = ZoneId.of("Brazil/East");

        LocalTime now1 = LocalTime.now(zone1);
        LocalTime now2 = LocalTime.now(zone2);
        System.out.println(now1);
        System.out.println(now2);

        System.out.println(now1.isBefore(now2));

        // 以小时、分钟为单位计算两个时间差
        long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
        long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);
        System.out.println(hoursBetween);
        System.out.println(minutesBetween);
    }

    // LocalDate 本地日期
    public static void localDate() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
        LocalDate yesterday = tomorrow.minusDays(2);

        System.out.println(today + " : " + tomorrow + " : " + yesterday);

        LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
        System.out.println(independenceDay);

        DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();

        System.out.println(dayOfWeek);    // FRIDAY

        DateTimeFormatter germanFormatter =
                DateTimeFormatter
                        .ofLocalizedDate(FormatStyle.MEDIUM)
                        .withLocale(Locale.GERMAN);

        LocalDate xmas = LocalDate.parse("24.12.2014", germanFormatter);
        System.out.println(xmas);   // 2014-12-24
    }

    // LocalDateTime 本地日期时间
    public static void localDateTime() {
        LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);
        System.out.println(sylvester);

        DayOfWeek dayOfWeek = sylvester.getDayOfWeek();
        System.out.println(dayOfWeek);      // WEDNESDAY

        Month month = sylvester.getMonth();
        System.out.println(month);          // DECEMBER

        long minuteOfDay = sylvester.getLong(ChronoField.MINUTE_OF_DAY);
        System.out.println(minuteOfDay);    // 1439

        Instant instant = sylvester
                .atZone(ZoneId.systemDefault())
                .toInstant();

        Date legacyDate = Date.from(instant);
        System.out.println(legacyDate);     // Wed Dec 31 23:59:59 CET 2014

        // 自定义格式
        DateTimeFormatter formatter =
                DateTimeFormatter
                        .ofPattern("MMM dd, yyyy - HH:mm");

        LocalDateTime parsed = LocalDateTime.parse("Nov 03, 2014 - 07:13", formatter);
        String string = formatter.format(parsed);
        System.out.println(string);     // Nov 03, 2014 - 07:13
    }
}
