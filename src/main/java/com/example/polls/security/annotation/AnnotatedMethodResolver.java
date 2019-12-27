package com.example.polls.security.annotation;

import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toUnmodifiableSet;

public class AnnotatedMethodResolver {

    private final Class<? extends Annotation> annotation;
    private final Map<String, Set<String>> beanNameToMethodSignatures = new HashMap<>();

    public AnnotatedMethodResolver(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    public void putIfAnnotationPresent(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        Set<String> annotatedMethods = Arrays.stream(beanClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .map(this::getSignatureString)
                .collect(toUnmodifiableSet());
        if (!annotatedMethods.isEmpty()) {
            beanNameToMethodSignatures.put(beanName, annotatedMethods);
        }
    }

    public Optional<Object> getProxyBeanWithHandlerBefore(Object bean, String beanName, Runnable invocationActionBefore) {
        Set<String> annotatedMethods = beanNameToMethodSignatures.get(beanName);
        if (!CollectionUtils.isEmpty(annotatedMethods)) {
            Class<?> beanClass = bean.getClass();

            InvocationHandler invocationHandler = (proxy, method, args) -> {
                if (annotatedMethods.contains(getSignatureString(method))) {
                    invocationActionBefore.run();
                }
                return method.invoke(bean, args);
            };
            Object proxyInstance = Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), invocationHandler);
            return Optional.of(proxyInstance);
        }
        return Optional.empty();
    }

    public Optional<Object> getProxyBeanWithHandlerAfter(Object bean, String beanName, Function<Object, Object> invocationActionAfter) {
        Set<String> annotatedMethods = beanNameToMethodSignatures.get(beanName);
        if (!CollectionUtils.isEmpty(annotatedMethods)) {
            Class<?> beanClass = bean.getClass();

            InvocationHandler invocationHandler = (proxy, method, args) -> {
                Object returnedValue = method.invoke(bean, args);
                if (annotatedMethods.contains(getSignatureString(method))) {
                    return invocationActionAfter.apply(returnedValue);
                }
                return returnedValue;
            };
            Object proxyInstance = Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), invocationHandler);
            return Optional.of(proxyInstance);
        }
        return Optional.empty();
    }

    public Optional<Object> getProxyBeanWithHandlers(Object bean, String beanName,
                                                     Runnable invocationActionBefore, Function<Object, Object> invocationActionAfter) {
        Set<String> annotatedMethods = beanNameToMethodSignatures.get(beanName);
        if (!CollectionUtils.isEmpty(annotatedMethods)) {
            Class<?> beanClass = bean.getClass();

            InvocationHandler invocationHandler = (proxy, method, args) -> {
                if (annotatedMethods.contains(getSignatureString(method))) {
                    invocationActionBefore.run();
                }
                Object returnedValue = method.invoke(bean, args);
                if (annotatedMethods.contains(getSignatureString(method))) {
                    return invocationActionAfter.apply(returnedValue);
                }
                return returnedValue;
            };
            Object proxyInstance = Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), invocationHandler);
            return Optional.of(proxyInstance);
        }
        return Optional.empty();
    }

    private String getSignatureString(Method method) {
        StringBuilder sb = new StringBuilder(method.getName());
        sb.append('(');
        StringJoiner sj = new StringJoiner(",");
        for (Class<?> parameterType : method.getParameterTypes()) {
            sj.add(parameterType.getTypeName());
        }
        sb.append(sj);
        sb.append(')');
        return sb.toString();
    }
}
