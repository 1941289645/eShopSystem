package com.example.springboot.service.impl;

import com.example.springboot.entity.Cart;
import com.example.springboot.mapper.CartMapper;
import com.example.springboot.service.ICartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lhc
 * @since 2023-06-21
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

}
