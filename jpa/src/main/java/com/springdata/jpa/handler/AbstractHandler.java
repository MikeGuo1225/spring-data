package com.springdata.jpa.handler;

import com.springdata.jpa.dto.OrderDTO;

public abstract class AbstractHandler {
    abstract public String handler(OrderDTO dto);
}
