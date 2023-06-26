package com.example.springboot.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.UserDTO;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.springboot.service.IMembersService;
import com.example.springboot.entity.Members;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lhc
 * @since 2023-05-06
 */
@RestController
@RequestMapping("/members")
public class MembersController {

    @Resource
    private IMembersService membersService;

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO){
        String username= userDTO.getLogin();
        String password=userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDTO dto = membersService.login(userDTO);
        System.out.println(dto);
        return Result.success(dto);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO){//@RequestBody将前台josn对象转换为后台的java对象
        String username= userDTO.getLogin();
        String password=userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        return Result.success(membersService.register(userDTO));
    }



    //新增和修改
    @PostMapping
    public boolean save(@RequestBody Members members){//@RequestBody将前台josn对象转换为后台的java对象
        //新增或者更新
        return membersService.saveOrUpdate(members);
    }

    //删除指定deptid的数据
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){  //@PathVariable这个注解表示上面url这个参数，上面花括号里面的id和这个参数id是一一对应的
        return membersService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> deptids){
        return membersService.removeByIds(deptids);
    }

    //查询所有数据
    @GetMapping
    public List<Members> findAll(){
        //List<Department> all=departmentMapper.findAll();
        return membersService.list();
    }

    @GetMapping("/{id}")
    public Members findeOne(@PathVariable Integer id){
        return membersService.getById(id);
    }

    @GetMapping("/id/{id}")
    public Result findOne(@PathVariable Integer id){
        QueryWrapper<Members> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Members members = membersService.getOne(queryWrapper);
        return Result.success(members);
    }

    //分页查询-mybatis-plus的方式进行
    @GetMapping("/page")
    public Page<Members> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize){
        QueryWrapper<Members> queryWrapper= new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return membersService.page(new Page<>(pageNum,pageSize),queryWrapper);
    }

}

