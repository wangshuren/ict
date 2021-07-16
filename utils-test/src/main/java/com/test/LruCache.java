package com.test;

import java.util.LinkedHashMap;

/**
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/7/12 0012 9:36
 */
public class LruCache<K, V> extends LinkedHashMap<K, V> {
    /** 最大容量 */
    private int maxCapacity;

    public LruCache(int maxCapacity) {
        super(16, 0.75f, true);
        this.maxCapacity = maxCapacity;
    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * 当列表中的元素个数大于指定的最大容量时，返回true,并将最老的元素删除。
     */
    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        if (super.size() > maxCapacity) {
            return true;
        }
        return false;
    }

}

