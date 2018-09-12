package com.ryy.test;

import com.ryy.annotation.TestAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by daojia on 2018/8/21.
 * 类说明
 */
@TestAnnotation
public class ProxyTest {
    /**
     * 方法说明
     * @param args
     */
    public static void main(String[] args) {
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Class<MyInterface>[] clazzArr = new Class[] {MyInterface.class,TestInterface.class};
        MyInterface o = (MyInterface)Proxy.newProxyInstance(ProxyTest.class.getClassLoader(), clazzArr, new MyHandler(new People()));
        o.say();
    }
}

/**
 * 测试内部类
 */
class MyHandler implements InvocationHandler{
    private People people ;
//    public MyHandler(Student student){
//        this.student = student;
//    }

    /**
     * 测试方法
     * @param people
     */
    public MyHandler(People people){
        this.people = people;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        MyInterface proxy1 = (MyInterface) proxy;
//        proxy1.say();
        System.out.println("代理方法前置+++++++");
//        Thread.sleep(10000);
//        Object invoke = method.invoke(proxy,args);
        Object invoke = method.invoke(people,args);
//        student.cry();
        System.out.println("代理方法后置-------");
        return null;
    }
}
interface TestInterface{
    void eat();
}
interface MyInterface{
    void say();
}
class People implements MyInterface{

    @Override
    public void say() {
        System.out.println("people say !!!!");
    }
}
class Student {
    public void cry() {
        System.out.println("student cry !!!!");
    }
}
