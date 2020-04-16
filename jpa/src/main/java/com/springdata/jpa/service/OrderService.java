package com.springdata.jpa.service;

import com.springdata.jpa.dto.OrderDTO;

public interface OrderService {
    String handle(OrderDTO dto);
}
