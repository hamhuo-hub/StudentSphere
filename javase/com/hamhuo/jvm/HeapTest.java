package com.hamhuo.jvm;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HeapTest {
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(30000);
        System.out.println(1);
        Integer[] arr = new Integer[]{1,2,3,4,5,6,7,8,9};
        Thread.sleep(5000);
        System.out.println(2);
        System.gc();
        Thread.sleep(5000);
        System.out.println(3);
        arr = null;
        Thread.sleep(5000);
        System.out.println(4);
        System.gc();
    }
}
