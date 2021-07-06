package com.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/6/29 0029 11:14
 */
public class ListTest {
    public static void main(String[] args) {
        List<String> lis = new ArrayList<>();
        lis.add("11");
        lis.add("9");
        lis.add(null);
        lis.add("1");
        lis.add("1");
        lis.add(null);
        lis.add("2");
        lis.add("6");
        System.out.println(lis);

        Set<String> ss = new HashSet<>();

        ss.add("9");
        ss.add(null);
        ss.add("1");
        ss.add("1");
        ss.add(null);
        ss.add("2");
        ss.add("6");
        ss.add("11");
        System.out.println(ss);

    }
}
