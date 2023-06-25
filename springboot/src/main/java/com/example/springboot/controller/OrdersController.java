package com.example.springboot.controller;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.controller.enums.OrderStatusEnum;
import com.example.springboot.entity.*;
import com.example.springboot.service.IOrderdetailsService;
import com.example.springboot.service.IProductsService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.springboot.service.IOrdersService;

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
@RequestMapping("/orders")
public class OrdersController {

    @Resource
    private IOrdersService ordersService;

    @Resource
    private IOrderdetailsService orderdetailsService;

    @Resource
    private IProductsService productsService;

    //新增和修改
    @PostMapping
    public boolean save(@RequestBody Orders orders){//@RequestBody将前台josn对象转换为后台的java对象
        //新增或者更新
        return ordersService.saveOrUpdate(orders);
    }

    @PostMapping("/addOrder")
    public Result addOrder(@RequestBody List<Cart> carts, @RequestParam Map<String, Object> form){
        ordersService.addOrder(carts,form);
        return Result.success();
    }

    @PostMapping("/status")
    public Result updateOrder(@RequestBody Orders orders){
        ordersService.updateOrder(orders);
        return Result.success();
    }


    //删除指定deptid的数据
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){  //@PathVariable这个注解表示上面url这个参数，上面花括号里面的id和这个参数id是一一对应的
        return ordersService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> deptids){
        return ordersService.removeByIds(deptids);
    }

    //查询所有数据
    @GetMapping
    public List<Orders> findAll(){
        //List<Department> all=departmentMapper.findAll();
        return ordersService.list();
    }

    @GetMapping("/{id}")
    public Orders findeOne(@PathVariable Integer id){
        return ordersService.getById(id);
    }

    //分页查询-mybatis-plus的方式进行
    @GetMapping("/page")
    public Page<Orders> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                 @RequestParam (defaultValue = "")String orderId){
        QueryWrapper<Orders> queryWrapper= new QueryWrapper<>();
        queryWrapper.orderByDesc("auto_id");
        queryWrapper.like("order_id",orderId);

        Members currentMember= TokenUtils.getCurrentUser();
        if (currentMember.getRole().equals("user")){
            queryWrapper.eq("member_id",currentMember.getId());
        }

        Page<Orders> page = ordersService.page(new Page<>(pageNum, pageSize), queryWrapper);
        for (Orders record:page.getRecords()){
            String id=record.getOrderId();
            List<Orderdetails> orderdetails=orderdetailsService.list(new QueryWrapper<Orderdetails>().eq("order_id",id));

            for (Orderdetails temp: orderdetails){
                Products product = productsService.getById(temp.getProductId());
                temp.setProductName(product.getName());
            }
            record.setOrderdetails(orderdetails);
        }

        return page;
    }

}

