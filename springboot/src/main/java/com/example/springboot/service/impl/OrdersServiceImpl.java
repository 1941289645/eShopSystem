package com.example.springboot.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.controller.enums.OrderStatusEnum;
import com.example.springboot.entity.Cart;
import com.example.springboot.entity.Orderdetails;
import com.example.springboot.entity.Orders;
import com.example.springboot.entity.Products;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.OrdersMapper;
import com.example.springboot.service.ICartService;
import com.example.springboot.service.IOrderdetailsService;
import com.example.springboot.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.service.IProductsService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.rowset.serial.SerialException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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
    @Resource
    private IProductsService productsService;
    @Autowired
    private ICartService cartService;
    @Override
    public void addOrder(List<Cart> carts) {
        BigDecimal total=new BigDecimal(0);
        Orders orders = new Orders();
        orders.setOrderId(IdUtil.fastSimpleUUID());//设置订单Id
        orders.setMemberId(TokenUtils.getCurrentUser().getId());
        orders.setStatus(OrderStatusEnum.NEED_PAY.getCode());     //设置待支付状态
        for (Cart cart:carts){
            total=total.add(cart.getPrice().multiply(BigDecimal.valueOf(cart.getNum())));

            //下单成功，扣库存
            Products products = productsService.getById(cart.getProductId());
            if(products.getNums()< cart.getNum()){
                throw new ServiceException("-1","库存不足");
            }
            products.setNums(products.getNums()- cart.getNum());
            productsService.updateById(products);

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
        save(orders);   //保存订单


    }

    @Override
    public void updateOrder(Orders orders) {
        if (Objects.equals(orders.getStatus(), OrderStatusEnum.CANCEL.getCode())){
            //取消订单，加库存
            String orderId = orders.getOrderId();
            List<Orderdetails> orderdetails = orderdetailsService.list(new QueryWrapper<Orderdetails>().eq("order_id", orderId));
            for (Orderdetails orderdetail:orderdetails){
                Products products = productsService.getById(orderdetail.getProductId());
                products.setNums(products.getNums()+ orderdetail.getAmount());
                productsService.updateById(products);
            }
        }
        updateById(orders);
    }
}
