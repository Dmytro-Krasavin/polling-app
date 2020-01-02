package com.example.polls.security.annotation;

import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.UnaryOperator;

import static java.util.stream.Collectors.toUnmodifiableSet;

@RequiredArgsConstructor
public class AnnotatedMethodResolver {

    private final Class<? extends Annotation> annotationClass;
    private final Map<String, Set<String>> beanNameToMethodSignatures = new HashMap<>();

    public void registerAnnotatedMethods(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        Set<String> annotatedMethods = Arrays.stream(beanClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotationClass))
                .map(this::getSignatureString)
                .collect(toUnmodifiableSet());
        if (!annotatedMethods.isEmpty()) {
            beanNameToMethodSignatures.put(beanName, annotatedMethods);
        }
    }

    public Optional<Object> getProxyBeanWithHandlerBefore(Object bean, String beanName, Runnable invocationActionBefore) {
        return getProxyBean(bean, beanName, invocationActionBefore, null);
    }

    public Optional<Object> getProxyBeanWithHandlerAfter(Object bean, String beanName, UnaryOperator<Object> invocationActionAfter) {
        return getProxyBean(bean, beanName, null, invocationActionAfter);
    }

    public Optional<Object> getProxyBean(Object bean, String beanName,
                                         @Nullable Runnable invocationActionBefore, @Nullable UnaryOperator<Object> invocationActionAfter) {
        Set<String> annotatedMethods = beanNameToMethodSignatures.get(beanName);
        if (!CollectionUtils.isEmpty(annotatedMethods)) {
            Class<?> beanClass = bean.getClass();
            if (hasInterface(beanClass)) {
                Object proxyInstance = generateDynamicProxy(bean, annotatedMethods, invocationActionBefore, invocationActionAfter);
                return Optional.of(proxyInstance);
            }
            Object proxyInstance = generateCglibInstance(bean, annotatedMethods, invocationActionBefore, invocationActionAfter);
            return Optional.of(proxyInstance);
        }
        return Optional.empty();
    }

    private Object generateDynamicProxy(Object bean, Set<String> annotatedMethods,
                                        Runnable invocationActionBefore, UnaryOperator<Object> invocationActionAfter) {
        Class<?> beanClass = bean.getClass();
        InvocationHandler invocationHandler = (proxy, method, args) -> {
            boolean methodAnnotated = isMethodAnnotated(method, annotatedMethods);
            if (methodAnnotated && invocationActionBefore != null) {
                invocationActionBefore.run();
            }
            Object returnedValue = method.invoke(bean, args);
            if (methodAnnotated && invocationActionAfter != null) {
                return invocationActionAfter.apply(returnedValue);
            }
            return returnedValue;
        };
        return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), invocationHandler);
    }

    private Object generateCglibInstance(Object bean, Set<String> annotatedMethods,
                                         Runnable invocationActionBefore, UnaryOperator<Object> invocationActionAfter) {
        throw new IllegalStateException("Not implemented");
    }

    private boolean isMethodAnnotated(Method method, Set<String> annotatedMethods) {
        return annotatedMethods.contains(getSignatureString(method));
    }

    private boolean hasInterface(Class<?> beanClass) {
        return beanClass.getInterfaces().length > 0;
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
