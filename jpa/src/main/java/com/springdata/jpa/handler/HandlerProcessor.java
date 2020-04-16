package com.springdata.jpa.handler;

import com.google.common.collect.Maps;
import com.springdata.jpa.config.annotation.HandlerType;
import com.springdata.jpa.util.ClassScaner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 1、扫描指定包中标有@HandlerType的类；
 * 2、将注解中的类型值作为key，对应的类作为value，保存在Map中；
 * 3、以上面的map作为构造函数参数，初始化HandlerContext，将其注册到spring容器中；
 */
@Component
public class HandlerProcessor implements BeanFactoryPostProcessor {
    private static final String[] HANDLER_PACKAGE = {"com.springdata.jpa.handler.businessHandler"};
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, Class> handlerMap = Maps.newHashMapWithExpectedSize(2);
        ClassScaner.scan(HANDLER_PACKAGE, HandlerType.class).forEach(clazz -> {
            String type = clazz.getAnnotation(HandlerType.class).value();
            handlerMap.put(type, clazz);
        });

        HandlerContext context = new HandlerContext(handlerMap);
        beanFactory.registerSingleton(HandlerContext.class.getName(), context);
    }
}
