package com.springdata.jpa.service.impl;

import com.springdata.jpa.dto.OrderDTO;
import com.springdata.jpa.handler.AbstractHandler;
import com.springdata.jpa.handler.HandlerContext;
import com.springdata.jpa.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private HandlerContext handlerContext;

    @Override
    public String handle(OrderDTO dto) {
        AbstractHandler handler = handlerContext.getInstance(dto.getType());
        return handler.handler(dto);
    }
}
