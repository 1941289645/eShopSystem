package com.example.springboot.controller;


import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Members;
import com.example.springboot.entity.Products;
import com.example.springboot.service.IProductsService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.springboot.service.ICartService;
import com.example.springboot.entity.Cart;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lhc
 * @since 2023-06-21
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Resource
    private ICartService cartService;

    @Resource
    private IProductsService productsService;

    //新增和修改
    @PostMapping
    public Result save(@RequestBody Cart cart){//@RequestBody将前台josn对象转换为后台的java对象
        //新增或者更新
        return Result.success(cartService.saveOrUpdate(cart));
    }

    //删除指定deptid的数据
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){  //@PathVariable这个注解表示上面url这个参数，上面花括号里面的id和这个参数id是一一对应的
        return cartService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> deptids){
        return cartService.removeByIds(deptids);
    }

    //查询所有数据
    @GetMapping
    public Result findAll(){
        List<Cart> list = cartService.list();
        for (Cart cart: list){
            Products product = productsService.getById(cart.getProductId());
            cart.setProductName(product.getName());
            cart.setImage(product.getImage());
            cart.setPrice(product.getPrice());
        }
        return Result.success(list);
    }

    @GetMapping("/personCart")
    public Result personCart(){
        BigDecimal total=new BigDecimal(0);
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        Members currentMember= TokenUtils.getCurrentUser();
        queryWrapper.eq("member_id", currentMember.getId());
        List<Cart> list = cartService.list(queryWrapper);
        for (Cart cart: list){
            Products product = productsService.getById(cart.getProductId());
            cart.setProductName(product.getName());
            cart.setImage(product.getImage());
            cart.setPrice(product.getPrice());
            total=total.add(product.getPrice().multiply(BigDecimal.valueOf(cart.getNum())));
        }
        Dict dict=Dict.create().set("list",list).set("total",total);
        return Result.success(dict);
    }

    @PostMapping("/cal")
    public Result cal(@RequestBody List<Cart> carts){
        BigDecimal total=new BigDecimal(0);
        for (Cart cart: carts){
            Products product = productsService.getById(cart.getProductId());
            total=total.add(product.getPrice().multiply(BigDecimal.valueOf(cart.getNum())));
        }
        return Result.success(total);
    }

    @GetMapping("/{id}")
    public Cart findeOne(@PathVariable Integer id){
        return cartService.getById(id);
    }

    //分页查询-mybatis-plus的方式进行
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize){
        QueryWrapper<Cart> queryWrapper= new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        Members currentMember= TokenUtils.getCurrentUser();

        System.out.println(currentMember.getId());
        queryWrapper.eq("member_id",currentMember.getId());

        Page<Cart> page = cartService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Cart> records=page.getRecords();
        for (Cart record: records){
            Products product = productsService.getById(record.getProductId());
            record.setProductName(product.getName());
            record.setImage(product.getImage());
            record.setPrice(product.getPrice());
        }
        return Result.success(page);
    }

}

