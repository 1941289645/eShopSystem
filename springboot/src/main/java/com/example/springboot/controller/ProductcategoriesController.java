package com.example.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.springboot.service.IProductcategoriesService;
import com.example.springboot.entity.Productcategories;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lhc
 * @since 2023-05-07
 */
@RestController
@RequestMapping("/productcategories")
public class ProductcategoriesController {

    @Resource
    private IProductcategoriesService productcategoriesService;

    //新增和修改
    @PostMapping
    public boolean save(@RequestBody Productcategories productcategories){//@RequestBody将前台josn对象转换为后台的java对象
        //新增或者更新
        return productcategoriesService.saveOrUpdate(productcategories);
    }

    //删除指定deptid的数据
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){  //@PathVariable这个注解表示上面url这个参数，上面花括号里面的id和这个参数id是一一对应的
        return productcategoriesService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> deptids){
        return productcategoriesService.removeByIds(deptids);
    }

    //查询所有数据
    @GetMapping
    public Result findAll(){
        //List<Department> all=departmentMapper.findAll();
        return Result.success(productcategoriesService.list());
    }

    @GetMapping("/{id}")
    public Productcategories findeOne(@PathVariable Integer id){
        return productcategoriesService.getById(id);
    }

    //分页查询-mybatis-plus的方式进行
    @GetMapping("/page")
    public Page<Productcategories> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize){
        QueryWrapper<Productcategories> queryWrapper= new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return productcategoriesService.page(new Page<>(pageNum,pageSize),queryWrapper);
    }

}

