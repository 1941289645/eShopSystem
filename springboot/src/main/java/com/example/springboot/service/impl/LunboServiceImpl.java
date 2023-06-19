package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Lunbo;
import com.example.springboot.mapper.LunboMapper;
import com.example.springboot.service.ILunboService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 前台轮播图 服务实现类
 * </p>
 *
 * @author lhc
 * @since 2023-06-12
 */
@Service
public class LunboServiceImpl extends ServiceImpl<LunboMapper, Lunbo> implements ILunboService {

    @Resource
    private LunboMapper lunboMapper;

    @Override
    public Page<Lunbo> findpage(Page<Lunbo> page, String name) {
        return lunboMapper.findPage(page,name);
    }
}
