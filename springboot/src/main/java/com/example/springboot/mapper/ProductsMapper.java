package com.example.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Products;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lhc
 * @since 2023-05-07
 */
@Mapper
public interface ProductsMapper extends BaseMapper<Products> {

    Page<Products> findPage(Page<Products> page, @Param("name") String name);
}
