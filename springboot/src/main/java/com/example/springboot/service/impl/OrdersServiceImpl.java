package com.example.springboot.service.impl;

import cn.hutool.core.util.IdUtil;
import com.example.springboot.controller.enums.OrderStatusEnum;
import com.example.springboot.entity.Cart;
import com.example.springboot.entity.Orderdetails;
import com.example.springboot.entity.Orders;
import com.example.springboot.mapper.OrdersMapper;
import com.example.springboot.service.ICartService;
import com.example.springboot.service.IOrderdetailsService;
import com.example.springboot.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lhc
 * @since 2023-06-22
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

    @Autowired
    private IOrderdetailsService orderdetailsService;

    @Autowired
    private ICartService cartService;
    @Override
    public void addOrder(List<Cart> carts) {
        BigDecimal total=new BigDecimal(0);
        Orders orders = new Orders();
        orders.setOrderId(IdUtil.fastSimpleUUID());
        orders.setMemberId(TokenUtils.getCurrentUser().getId());
        orders.setStatus(OrderStatusEnum.NEED_PAY.getCode());
        for (Cart cart:carts){
            total=total.add(cart.getPrice().multiply(BigDecimal.valueOf(cart.getNum())));

            //设置订单详情信息
            Orderdetails orderdetails = new Orderdetails();
            orderdetails.setOrderId(orders.getOrderId());
            orderdetails.setImage(cart.getImage());
            orderdetails.setAmount(cart.getNum());
            orderdetails.setProductId(cart.getProductId());
            orderdetails.setPrice(cart.getPrice());
            orderdetailsService.save(orderdetails);

            //下单成功后删除购物车商品
            cartService.removeById(cart.getId());
        }
        orders.setMoney(total);//计算总价格
        save(orders);   //保存订单 获取订单id


    }
}
