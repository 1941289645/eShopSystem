package com.example.springboot.controller;


import com.example.springboot.entity.Student;
import com.example.springboot.service.IStudentService;
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
@RequestMapping("/student")
public class StudentController {

    @Resource
    private IStudentService studentService;

    //新增和修改
    @PostMapping
    public boolean save(@RequestBody Student student){//@RequestBody将前台josn对象转换为后台的java对象
        //新增或者更新
        return studentService.saveOrUpdate(student);
    }

    //删除指定deptid的数据
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){  //@PathVariable这个注解表示上面url这个参数，上面花括号里面的id和这个参数id是一一对应的
        return studentService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> deptids){
        return studentService.removeByIds(deptids);
    }

    //查询所有数据
    @GetMapping
    public List<Student> findAll(){
        //List<Department> all=departmentMapper.findAll();
        return studentService.list();
    }

    @GetMapping("/{id}")
    public Student findeOne(@PathVariable Integer id){
        return studentService.getById(id);
    }

    //分页查询-mybatis-plus的方式进行
//    @GetMapping("/page")
//    public Page<Student> findPage(@RequestParam Integer pageNum,
//                                  @RequestParam Integer pageSize){
//        QueryWrapper<Student> queryWrapper= new QueryWrapper<>();
//        queryWrapper.orderByDesc("studentid");
//        return studentService.page(new Page<>(pageNum,pageSize),queryWrapper);
//    }

}

