package com.example.springboot.service.impl;

import com.example.springboot.entity.Orderdetails;
import com.example.springboot.mapper.OrderdetailsMapper;
import com.example.springboot.service.IOrderdetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lhc
 * @since 2023-06-22
 */
@Service
public class OrderdetailsServiceImpl extends ServiceImpl<OrderdetailsMapper, Orderdetails> implements IOrderdetailsService {

}
