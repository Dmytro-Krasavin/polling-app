package com.example.polls.security.annotation;

import com.example.polls.exception.ForbiddenException;
import com.example.polls.security.service.LoginAttemptService;
import com.example.polls.util.IpAddressExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckClientIPAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final AnnotatedMethodResolver methodResolver = new AnnotatedMethodResolver(CheckClientIP.class);
    private final LoginAttemptService loginAttemptService;
    private final IpAddressExtractor ipAddressExtractor;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        methodResolver.putIfAnnotationPresent(bean, beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return methodResolver.getProxyBeanWithHandlerBefore(bean, beanName, this::checkClientIP).orElse(bean);
    }

    private void checkClientIP() {
        String ip = ipAddressExtractor.getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new ForbiddenException("Client IP address is blocked");
        }
    }
}
