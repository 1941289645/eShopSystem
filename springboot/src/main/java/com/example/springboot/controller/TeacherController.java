package com.example.springboot.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.entity.Teacher;
import com.example.springboot.service.ITeacherService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lhc
 * @since 2023-04-15
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private ITeacherService teacherService;

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO){
        String username= userDTO.getUsername();
        String password=userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDTO dto = teacherService.login(userDTO);
        return Result.success(dto);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO){//@RequestBody将前台josn对象转换为后台的java对象
        String username= userDTO.getUsername();
        String password=userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        return Result.success(teacherService.register(userDTO));
    }

    //新增和修改
    @PostMapping
    public boolean save(@RequestBody Teacher teacher){//@RequestBody将前台josn对象转换为后台的java对象
        //新增或者更新
        return teacherService.saveOrUpdate(teacher);
    }

    //删除指定deptid的数据
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){  //@PathVariable这个注解表示上面url这个参数，上面花括号里面的id和这个参数id是一一对应的
        return teacherService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> deptids){
        return teacherService.removeByIds(deptids);
    }

    //查询所有数据
    @GetMapping
    public List<Teacher> findAll(){
        //List<Department> all=departmentMapper.findAll();
        return teacherService.list();
    }

    @GetMapping("/{id}")
    public Teacher findeOne(@PathVariable Integer id){
        return teacherService.getById(id);
    }

    @GetMapping("/id/{id}")
    public Result findOne(@PathVariable Integer id){
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Tnumber",id);
        Teacher teacher = teacherService.getOne(queryWrapper);
        return Result.success(teacher);
    }

    //分页查询-mybatis-plus的方式进行
//    @GetMapping("/page")
//    public Page<Teacher> findPage(@RequestParam Integer pageNum,
//                                    @RequestParam Integer pageSize){
//        QueryWrapper<Teacher> queryWrapper= new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        return teacherService.page(new Page<>(pageNum,pageSize),queryWrapper);
//    }

}

