package com.example.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Members;
import com.example.springboot.entity.Productcategories;
import com.example.springboot.entity.Teacher;
import com.example.springboot.mapper.ProductsMapper;
import com.example.springboot.service.IProductcategoriesService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.springboot.service.IProductsService;
import com.example.springboot.entity.Products;

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
@RequestMapping("/products")
public class ProductsController {

    @Resource
    private IProductsService productsService;

    @Resource
    private ProductsMapper productsMapper;

    private IProductcategoriesService productcategoriesService;

    //新增和修改
    @PostMapping
    public boolean save(@RequestBody Products products){//@RequestBody将前台josn对象转换为后台的java对象
        //新增或者更新
        return productsService.saveOrUpdate(products);
    }

    @PostMapping("/update")
    public Result save2(@RequestBody Products products){
        //更新
        return Result.success(productsMapper.updateById(products));
    }

    //删除指定deptid的数据
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){  //@PathVariable这个注解表示上面url这个参数，上面花括号里面的id和这个参数id是一一对应的
        return productsService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return productsService.removeByIds(ids);
    }

    //查询所有数据
    @GetMapping
    public List<Products> findAll(){
        //List<Department> all=departmentMapper.findAll();
        return productsService.list();
    }

    @GetMapping("/{id}")
    public Products findeOne(@PathVariable Integer id){
        return productsService.getById(id);
    }

    //分页查询-mybatis-plus的方式进行
    @GetMapping("/page")
    public Page<Products> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                   @RequestParam (defaultValue = "")String name){
//        QueryWrapper<Products> queryWrapper= new QueryWrapper<>();
//        if(!"".equals(name)){
//            queryWrapper.like("name",name);
//        }
//        queryWrapper.orderByDesc("id");
//        Page<Products> page=productsService.page(new Page<>(pageNum,pageSize),queryWrapper);
//        List<Products> records=page.getRecords();
//        for(Products record: records){
//            Productcategories productcategories=productcategoriesService.getById(record.getProductCategoryId());
//            if (productcategories!=null){
//                record.setCategoryName(productcategories.getName());
//            }
//        }

        Page<Products> page=productsService.findPage(new Page<>(pageNum,pageSize),name);

        return page;
    }


}

