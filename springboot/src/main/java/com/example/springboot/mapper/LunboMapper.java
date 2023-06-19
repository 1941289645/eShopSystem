package com.example.springboot.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Lunbo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 前台轮播图 Mapper 接口
 * </p>
 *
 * @author lhc
 * @since 2023-06-12
 */
public interface LunboMapper extends BaseMapper<Lunbo> {

    Page<Lunbo> findPage(Page<Lunbo> page, String name);
}
