package com.example.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Products;
import com.example.springboot.mapper.LunboMapper;
import com.example.springboot.mapper.ProductsMapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.springboot.service.ILunboService;
import com.example.springboot.entity.Lunbo;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前台轮播图 前端控制器
 * </p>
 *
 * @author lhc
 * @since 2023-06-12
 */
@RestController
@RequestMapping("/lunbo")
public class LunboController {

    @Resource
    private ILunboService lunboService;

    @Resource
    private LunboMapper lunboMapper;

    //新增和修改
    @PostMapping
    public boolean save(@RequestBody Lunbo lunbo){//@RequestBody将前台josn对象转换为后台的java对象
        //新增或者更新
        return lunboService.saveOrUpdate(lunbo);
    }

    @PostMapping("/update")
    public Result save2(@RequestBody Lunbo lunbo){
        //更新
        return Result.success(lunboMapper.updateById(lunbo));
    }

    //删除指定deptid的数据
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){  //@PathVariable这个注解表示上面url这个参数，上面花括号里面的id和这个参数id是一一对应的
        return lunboService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> deptids){
        return lunboService.removeByIds(deptids);
    }

    //查询所有数据
    @GetMapping
    public List<Lunbo> findAll(){
        //List<Department> all=departmentMapper.findAll();
        return lunboService.list();
    }

    @GetMapping("/{id}")
    public Lunbo findeOne(@PathVariable Integer id){
        return lunboService.getById(id);
    }

    //分页查询-mybatis-plus的方式进行
    @GetMapping("/page")
    public Page<Lunbo> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam (defaultValue = "")String name){
//        QueryWrapper<Lunbo> queryWrapper= new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");

        Page<Lunbo> page=lunboService.findpage(new Page<>(pageNum,pageSize),name);
        return page;
    }

}

