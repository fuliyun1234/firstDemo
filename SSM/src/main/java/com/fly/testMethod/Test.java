package com.fly.testMethod;

import com.fly.pojo.UserInfo;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
//import org.apache.commons.collections.CollectionUtils; //

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    static List<UserInfo> userInfoList = new ArrayList<>();
    static {
        userInfoList.add(new UserInfo(1, "Bob", "alice123", 25));
        userInfoList.add(new UserInfo(2, "Alice", "bob456", 28));
        userInfoList.add(new UserInfo(3, "Charlie", "charlie789", 22));

        userInfoList.add(new UserInfo(4, "David", "david101", 18));
        userInfoList.add(new UserInfo(5, "Eve", "eve202", 12));
    }


    //点main方法时候，先进行Test类加载，加载的时候 初始化 static 静态变量，然后再执行main方法。
    public static void main(String[] args) {
        String text = null;

        //判断集合为 空 或 null 优先使用 spring 的
        if (CollectionUtils.isEmpty(userInfoList)) {
            System.out.println("集合为空");
        }
        if (StringUtils.isEmpty(text)) {
            System.out.println("字符串为空");
        }

        //Java Stream API 语法
        //将list 中 年龄大于 20 的放到 list2 中
        System.out.println("取年龄 大于20的");
        List<UserInfo> list2 = userInfoList.stream().filter(us -> us.getAge() > 20).collect(Collectors.toList());
        list2.forEach(System.out::println);
        //Lambda 表达式写法
        list2.forEach(item -> System.out.println(item));

        // 按年龄排序
        System.out.println("按年龄排序");
        List<UserInfo> list3 = userInfoList.stream()
                .sorted(Comparator.comparingInt(UserInfo::getAge))
                .collect(Collectors.toList());
        list3.forEach(System.out::println);

        // 按年龄 倒叙排序
        System.out.println("按年龄 倒叙排序");
        List<UserInfo> list4 = userInfoList.stream()
                .sorted(Comparator.comparingInt(UserInfo::getAge).reversed())
                .collect(Collectors.toList());
        list4.forEach(System.out::println);



        // 按年龄排序,之后 再按 性名排序
        System.out.println("按年龄排序,之后 再按 性名排序");
        List<UserInfo> list5 = userInfoList.stream()
                .sorted(Comparator
                        .comparingInt(UserInfo::getAge)
                        .thenComparing(UserInfo::getName)
                )
                .collect(Collectors.toList());
        list5.forEach(System.out::println);

    }
}
