package com.ryy.test;

import com.ryy.annotations.TestAnnotation;

import com.ryy.annotation.TestAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by daojia on 2018/8/21.
 */
@TestAnnotation
public class ProxyTest {
//    public static void main(String[] args) {
//        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
//        Class<Human>[] interfaceArray = new Class[]{Human.class};
//        Human h = (Human)Proxy.newProxyInstance(ProxyTest.class.getClassLoader(), interfaceArray, new MyInvocationHandler(new Student()));
//        h.say();
//    }
    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Student student = new Student();
        TestAnnotation annotation = student.getClass().getAnnotation(TestAnnotation.class);
        System.out.println(annotation.name());
    }
}
interface Human{
    void say();
}
@TestAnnotation
class Student implements Human{

    @Override
    public void say() {
        System.out.println("student 说:");
    }
}
class MyInvocationHandler implements InvocationHandler {

    private Human human;
    MyInvocationHandler(Human human){
        this.human = human;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("清了清嗓子");
//        Human proxy1 = (Human) proxy;
//        proxy1.toString();
        human.say();
        System.out.println("大家好");
        return null;
    }
}