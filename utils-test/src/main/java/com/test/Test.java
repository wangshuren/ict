package com.test;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/7/12 0012 9:31
 */
public class Test {

//    public static void main(String[] args) {
////        Map<String, Object> map = new LinkedHashMap<String, Object>(16, 0.75F, true);
////
////        for (int i = 1; i <= 5; i++) {
////            map.put(i + "", i);
////        }
////        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
////        while (iterator.hasNext()) {
////            System.out.println(iterator.next().getValue());
////        }
////
////        map.get("2");
////        map.get("3");
////        // 最近经常使用的元素就放在后面，最近最少使用的就排在了链表的前面
////        System.out.println("===============split line==================");
////
////        Iterator<Map.Entry<String, Object>> iterator2 = map.entrySet().iterator();
////        while (iterator2.hasNext()) {
////            System.out.println(iterator2.next().getValue());
////        }
////    }


    public static void main(String[] args) {
        LruCache<String, Object> cache = new LruCache<String, Object>(10);

        for (int i = 1; i <= 15; i++) {
            cache.put(i + "", i);
        }

        // 此时访问指定KEY的元素
        cache.get("10");

        Iterator<Map.Entry<String, Object>> iterator = cache.entrySet().iterator();
        for (; iterator.hasNext();) {
            Map.Entry<String, Object> entry = iterator.next();
            System.out.println("key=" + entry.getKey() + ",value=" + entry.getValue());
        }
    }
}
