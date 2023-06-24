package com.example.springboot.controller;


import cn.hutool.core.lang.Dict;
import cn.hutool.core.swing.ScreenUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Members;
import com.example.springboot.entity.Orders;
import com.example.springboot.service.IMembersService;
import com.example.springboot.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.springboot.service.IOrderdetailsService;
import com.example.springboot.entity.Orderdetails;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lhc
 * @since 2023-06-22
 */
@RestController
@RequestMapping("/orderdetails")
public class OrderdetailsController {

    @Resource
    private IOrderdetailsService orderdetailsService;

    @Resource
    private IOrdersService ordersService;

    @Autowired
    private IMembersService membersService;

    //新增和修改
    @PostMapping
    public boolean save(@RequestBody Orderdetails orderdetails){//@RequestBody将前台josn对象转换为后台的java对象
        //新增或者更新
        return orderdetailsService.saveOrUpdate(orderdetails);
    }


    @PostMapping("/savecomment")
    public boolean savecomment(@RequestBody Orderdetails orderdetails){//@RequestBody将前台josn对象转换为后台的java对象
        Orders order = ordersService.getOne(new QueryWrapper<Orders>().eq("order_id", orderdetails.getOrderId()));
        order.setStatus(5);
        ordersService.updateById(order);
        return orderdetailsService.saveOrUpdate(orderdetails);
    }

    //删除指定deptid的数据
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){  //@PathVariable这个注解表示上面url这个参数，上面花括号里面的id和这个参数id是一一对应的
        return orderdetailsService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> deptids){
        return orderdetailsService.removeByIds(deptids);
    }

    //查询所有数据
    @GetMapping
    public List<Orderdetails> findAll(){
        //List<Department> all=departmentMapper.findAll();
        return orderdetailsService.list();
    }

    @GetMapping("/comment/{productId}")
    public Result comment(@PathVariable Integer productId){
        ArrayList<Object> comments=new ArrayList<>();
        List<Orderdetails> list = orderdetailsService.list(new QueryWrapper<Orderdetails>().eq("product_id",productId));
        for (Orderdetails orderdetails:list){
            String orderId=orderdetails.getOrderId();
            Orders orders = ordersService.getOne(new QueryWrapper<Orders>().eq("order_id", orderId));
            Integer memberId = orders.getMemberId();
            Members members = membersService.getById(memberId);
            if (StrUtil.isNotBlank(orderdetails.getComment())){
                comments.add(Dict.create().set("avatar",members.getAvatarurl()).set("member",members.getName()).set("comment",orderdetails.getComment()));
            }
        }
        return Result.success(comments);
    }

    @GetMapping("/{id}")
    public Orderdetails findeOne(@PathVariable Integer id){
        return orderdetailsService.getById(id);
    }

    //分页查询-mybatis-plus的方式进行
    @GetMapping("/page")
    public Page<Orderdetails> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize){
        QueryWrapper<Orderdetails> queryWrapper= new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return orderdetailsService.page(new Page<>(pageNum,pageSize),queryWrapper);
    }

}

