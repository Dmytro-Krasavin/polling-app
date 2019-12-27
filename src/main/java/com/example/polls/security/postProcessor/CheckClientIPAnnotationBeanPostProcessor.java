package com.example.polls.security.postProcessor;

import com.example.polls.exception.ForbiddenException;
import com.example.polls.security.annotation.CheckClientIP;
import com.example.polls.security.service.LoginAttemptService;
import com.example.polls.util.IpAddressExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

import static java.util.stream.Collectors.toUnmodifiableSet;

@Component
@RequiredArgsConstructor
public class CheckClientIPAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final Map<String, Set<String>> beanNameToMethodSignatures = new HashMap<>();

    private final LoginAttemptService loginAttemptService;
    private final IpAddressExtractor ipAddressExtractor;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Set<String> annotatedMethods = Arrays.stream(beanClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(CheckClientIP.class))
                .map(this::getSignatureString)
                .collect(toUnmodifiableSet());
        if (!annotatedMethods.isEmpty()) {
            beanNameToMethodSignatures.put(beanName, annotatedMethods);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Set<String> annotatedMethods = beanNameToMethodSignatures.get(beanName);
        if (!CollectionUtils.isEmpty(annotatedMethods)) {
            Class<?> beanClass = bean.getClass();
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(),
                    (proxy, method, args) -> {
                        if (annotatedMethods.contains(getSignatureString(method))) {
                            checkClientIP();
                        }
                        return method.invoke(bean, args);
                    });
        }
        return bean;
    }

    private void checkClientIP() {
        String ip = ipAddressExtractor.getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new ForbiddenException("Client IP address is blocked");
        }
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
