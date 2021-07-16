package com.test;

import org.springframework.util.StopWatch;

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/7/16 0016 9:27
 */
public class Java8Lambda {
    final static String salutation = "Hello! ";

    public static void main(String[] args) {
        StopWatch watch = new StopWatch();

        watch.start("创建线程");
        createThread();
        watch.stop();

        watch.start("实现接口的方法");
        type1();
        watch.stop();

        // lambda表达式原理
        // 实际上是通过invokedynamic指令来实现的，下面是lambda表达式的几种书写形式
        watch.start("lambda表达式的几种书写形式");
        lambdaTypes();
        watch.stop();

        // lambda和Stream
        watch.start("lambda和Stream");
        labdaStream();
        watch.stop();

        System.out.println(watch.prettyPrint());
    }

    public static void labdaStream() {
        // java7
        List<String> list = Arrays.asList("1one", "two", "three", "4four");
        for(String str : list){
            if(Character.isDigit(str.charAt(0))){
                System.out.println(str);
            }
        }
        // java8
        list.stream()// 1、得到容器的Stream
                .filter(str -> Character.isDigit(str.charAt(0))) // 2、选出以数字开头的字符串
                .forEach(str -> {// 输出字符串
                    System.out.println(str);
                    System.out.println("2 " + str);
                });
        List<String> newList =
            list.stream()// 1、得到容器的Stream
                    .filter(str -> !Character.isDigit(str.charAt(0))) // 2、选出以数字开头的字符串
                    .map(String::toUpperCase)// 3、转换成大写
                    .collect(Collectors.toList());//4、生成结果集
        newList.stream().forEach(str-> System.out.println("输出" + str));
    }

    public static void lambdaTypes(){
        Runnable run = () -> System.out.println("hello run"); // 1
        ActionListener listener = event -> System.out.println("button clicked"); // 2
        Runnable multiLine = () -> {// 3
            System.out.println("hello");
            System.out.println("run");
        };
        BinaryOperator<Long> add = (Long x, Long y) -> x + y; //4
        BinaryOperator<Long> addImplicit = (x,y) -> x + y; //5
    }

    public static void createThread(){
        new Thread(
                () -> System.out.println("Thread1 run()")
        ).start();

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Thread2 run()");
                    }
                }
        ).start();
    }

    public static void type1(){
        Java8Lambda tester = new Java8Lambda();
        // 类型声明
        MathOperation addition = (int a, int b) -> a + b;
        // 不用类型声明
        MathOperation subtraction = (a, b) -> a - b;
        // 大括号中的返回语句
        MathOperation multiplication = (int a, int b) -> { return a * b; };
        // 没有大括号及返回语句
        MathOperation division = (int a, int b) -> a / b;
        System.out.println("10 + 5 = " + tester.operate(10, 5, addition));
        System.out.println("10 - 5 = " + tester.operate(10, 5, subtraction));
        System.out.println("10 x 5 = " + tester.operate(10, 5, multiplication));
        System.out.println("10 / 5 = " + tester.operate(10, 5, division));
        // 不用括号  lambda 表达式只能引用标记了 final 的外层局部变量，
        // 这就是说不能在 lambda 内部修改定义在域外的局部变量，否则会编译错误
        // lambda 表达式的局部变量可以不用声明为 final，但是必须不可被后面的代码修改（即隐性的具有 final 的语义）
        GreetingService greetService1 = message ->
                System.out.println(salutation + message);
        // 用括号
        GreetingService greetService2 = (message) ->
                System.out.println("Hello " + message);
        greetService1.sayMessage("Runoob");
        greetService2.sayMessage("Google");
    }

    interface MathOperation {
        int operation(int a, int b);
    }

    interface GreetingService {
        void sayMessage(String message);
    }

    private int operate(int a, int b, MathOperation mathOperation){
        return mathOperation.operation(a, b);
    }


}
