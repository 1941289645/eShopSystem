package com.example.springboot.service;

import com.example.springboot.entity.Cart;
import com.example.springboot.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lhc
 * @since 2023-06-22
 */
public interface IOrdersService extends IService<Orders> {

    void addOrder(List<Cart> carts, Map<String, Object> form);

    void updateOrder(Orders orders);
}
