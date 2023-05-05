package com.example.springboot.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Lessonchoose;
import com.example.springboot.entity.Teacher;
import com.example.springboot.service.ILessonchooseService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lhc
 * @since 2023-04-18
 */
@RestController
@RequestMapping("/lessonchoose")
public class LessonchooseController {

    @Resource
    private ILessonchooseService lessonchooseService;

    //新增和修改
    @PostMapping
    public boolean save(@RequestBody Lessonchoose lessonchoose){//@RequestBody将前台josn对象转换为后台的java对象
        //新增或者更新
        UpdateWrapper<Lessonchoose> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("snumber",lessonchoose.getSnumber());
        updateWrapper.eq("lnumber",lessonchoose.getLnumber());
        updateWrapper.eq("tnumber",lessonchoose.getTnumber());
        return lessonchooseService.saveOrUpdate(lessonchoose,updateWrapper);
    }

    //删除指定deptid的数据
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){  //@PathVariable这个注解表示上面url这个参数，上面花括号里面的id和这个参数id是一一对应的
        return lessonchooseService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> deptids){
        return lessonchooseService.removeByIds(deptids);
    }

    //查询所有数据
    @GetMapping
    public List<Lessonchoose> findAll(){
        //List<Department> all=departmentMapper.findAll();
        return lessonchooseService.list();
    }

    @GetMapping("/{id}")
    public Lessonchoose findeOne(@PathVariable Integer id){
        return lessonchooseService.getById(id);
    }

    //分页查询-mybatis-plus的方式进行
    @GetMapping("/page")
    public Page<Lessonchoose> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam (defaultValue = "")String snumber,
                                       @RequestParam (defaultValue = "")String lnumber,
                                       @RequestParam (defaultValue = "")String tnumber){
        QueryWrapper<Lessonchoose> queryWrapper= new QueryWrapper<>();
        if(!"".equals(snumber)){
            queryWrapper.like("snumber",snumber);
        }
        if(!"".equals(lnumber)){
            queryWrapper.like("lnumber",lnumber);
            //queryWrapper.and(w -> w.like("address",address));
        }

        Teacher currentUser= TokenUtils.getCurrentUser();
        queryWrapper.like("tnumber",currentUser.getId());
//        queryWrapper.orderByDesc("id");
        return lessonchooseService.page(new Page<>(pageNum,pageSize),queryWrapper);
    }

}

