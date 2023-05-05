package com.example.springboot.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Department;
import com.example.springboot.entity.Teacher;
import com.example.springboot.service.IDepartmentService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lhc
 * @since 2023-04-08
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Resource
    private IDepartmentService departmentService;

    //新增和修改
    @PostMapping
    public boolean save(@RequestBody Department department){//@RequestBody将前台josn对象转换为后台的java对象
        //新增或者更新
        return departmentService.saveOrUpdate(department);
    }

    //删除指定deptid的数据
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){  //@PathVariable这个注解表示上面url这个参数，上面花括号里面的id和这个参数id是一一对应的
        return departmentService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> deptids){
        return departmentService.removeByIds(deptids);
    }

    //查询所有数据
    @GetMapping
    public List<Department> findAll(){
        //List<Department> all=departmentMapper.findAll();
        return departmentService.list();
    }

    @GetMapping("/{id}")
    public Department findeOne(@PathVariable Integer id){
        return departmentService.getById(id);
    }

    //分页查询-mybatis-plus的方式进行
    @GetMapping("/page")
    public Page<Department> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                     @RequestParam (defaultValue = "")String deptname,
                                     @RequestParam (defaultValue = "")String address,
                                     @RequestParam (defaultValue = "")String phonecode){
        QueryWrapper<Department> queryWrapper= new QueryWrapper<>();
        if(!"".equals(deptname)){
            queryWrapper.like("deptname",deptname);
        }
        if(!"".equals(address)){
            queryWrapper.like("address",address);
            //queryWrapper.and(w -> w.like("address",address));
        }
        if(!"".equals(phonecode)){
            queryWrapper.like("phonecode",phonecode);
        }
        queryWrapper.orderByDesc("deptid");

        //获取当前用户信息
        Teacher currentUser= TokenUtils.getCurrentUser();
        System.out.println("获取当前用户信息================="+currentUser.getTname());
        return departmentService.page(new Page<>(pageNum,pageSize),queryWrapper);
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //从数据库查询出所有的数据
        List<Department> list=departmentService.list();
        //通过工具类创建writer 写出到磁盘路径
        //ExcelWriter writer= ExcelUtil.getWriter(fileUploadPath + "/用户信息.xlsx");
        //在内存操作，写出到浏览器
        ExcelWriter writer= ExcelUtil.getWriter(true);
        //自定义标题列名
        writer.addHeaderAlias("deptid","学院号");
        writer.addHeaderAlias("deptname","学院名");
        writer.addHeaderAlias("address","地址");
        writer.addHeaderAlias("phonecode","电话号码");

        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list,true);

        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName= URLEncoder.encode("学院信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");

        ServletOutputStream out= response.getOutputStream();
        writer.flush(out,true);
        out.close();
        writer.close();
    }

    /**
     * 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws Exception{
        InputStream inputStream=file.getInputStream();
        ExcelReader reader=ExcelUtil.getReader(inputStream);
        //方式1：（推荐）通过javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<Department> list=reader.readAll(Department.class);

        //方式2：忽略表头的英文，直接读取表的内容
        List<List<Object>> list=reader.read(1);
        List<Department> departments = CollUtil.newArrayList();
        for (List<Object> row : list) {
            Department department=new Department();
            department.setDeptid(Integer.valueOf(row.get(0).toString()));
            department.setDeptname(row.get(1).toString());
            department.setAddress(row.get(2).toString());
            department.setPhonecode(row.get(3).toString());
            departments.add(department);
        }
        departmentService.saveBatch(departments);
        return true;
    }
}

