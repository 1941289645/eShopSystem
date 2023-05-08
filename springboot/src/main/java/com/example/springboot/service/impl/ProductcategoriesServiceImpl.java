package com.example.springboot.service.impl;

import com.example.springboot.entity.Productcategories;
import com.example.springboot.mapper.ProductcategoriesMapper;
import com.example.springboot.service.IProductcategoriesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lhc
 * @since 2023-05-07
 */
@Service
public class ProductcategoriesServiceImpl extends ServiceImpl<ProductcategoriesMapper, Productcategories> implements IProductcategoriesService {

}
