package com.easy.aspect;

import com.basic.exception.BaseException;
import com.easy.annotations.Validation;
import com.easy.interfaces.ValidInterface;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.*;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 参数校验的代理类
 *
 * @author LZH
 * @version 1.0.11
 * @since 2023/12/30
 */
@Aspect
@ConditionalOnProperty(name = "easy.valid")
public class ValidParams implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 方法执行之前的校验代理
     *
     * @param joinPoint  {@link JoinPoint}
     */
    @Before(value = "@annotation(com.easy.annotations.Validation)")
    public void doBefore(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Validation annotation = method.getAnnotation(Validation.class);
        Class<?> clazz = annotation.value();
        ValidInterface anInterface = (ValidInterface) applicationContext.getBean(clazz);
        Optional
            .of(anInterface)
            .orElseThrow(this::isNull)
            .handle(joinPoint.getArgs());
    }

    /**
     * 健壮型考虑
     *
     * @return {@link BaseException}
     */
    private BaseException isNull() {
        return new BaseException("The ValidInterface object is Null;");
    }

    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link org.springframework.beans.factory.InitializingBean#afterPropertiesSet()}
     * or a custom init-method. Invoked after {@link ResourceLoaderAware#setResourceLoader},
     * {@link ApplicationEventPublisherAware#setApplicationEventPublisher} and
     * {@link MessageSourceAware}, if applicable.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws BeansException if thrown by application context methods
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        ValidParams.applicationContext = applicationContext;
    }
}
