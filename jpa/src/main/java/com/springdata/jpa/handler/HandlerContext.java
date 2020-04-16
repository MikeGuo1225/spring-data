package com.springdata.jpa.handler;

import com.springdata.jpa.util.BeanTool;

import java.util.Map;

public class HandlerContext {
    private Map<String, Class> handleMap;

    public HandlerContext(Map<String, Class> handleMap) {
        this.handleMap = handleMap;
    }

    public AbstractHandler getInstance(String tyepe) {
        Class clazz = handleMap.get(tyepe);
        if (clazz == null) {
            throw new IllegalArgumentException("not found handler for type: " + tyepe);
        }
        return (AbstractHandler) BeanTool.getBean(clazz);
    }
}
