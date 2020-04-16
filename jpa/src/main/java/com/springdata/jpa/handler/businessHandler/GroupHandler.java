package com.springdata.jpa.handler.businessHandler;

import com.springdata.jpa.dto.OrderDTO;
import com.springdata.jpa.config.annotation.HandlerType;
import com.springdata.jpa.handler.AbstractHandler;
import org.springframework.stereotype.Component;

@Component
@HandlerType("group")
public class GroupHandler extends AbstractHandler {
    @Override
    public String handler(OrderDTO dto) {
        return null;
    }
}
