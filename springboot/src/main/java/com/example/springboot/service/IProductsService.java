package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Products;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lhc
 * @since 2023-05-07
 */
public interface IProductsService extends IService<Products> {

    Page<Products> findPage(Page<Products> page, String name);
}
