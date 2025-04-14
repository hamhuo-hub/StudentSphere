package com.hamhuo.jvm;

import java.util.ArrayList;

public class StackTest {
    public static void main(String[] args) {
        method3();
    }
    public static void method1() {
        method2(100);
    }
    public static void method2(int c) {
        int a = 1;
        c = a++;
    }

    static ArrayList<String> array = new ArrayList<>();
    public static void method3() {
        System.out.println(array.clone());
    }
}
