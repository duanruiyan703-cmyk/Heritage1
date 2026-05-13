package com.ruangong.heritage;

import java.util.ArrayList;
import java.util.List;

public class TestStream {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(40);

        List<Integer> list2 = list.stream()
                // lambda 表达式
                .map(num -> num * 2)
                .filter(num-> num>=60)
                .toList();
        System.out.println(list2);


    }
}
