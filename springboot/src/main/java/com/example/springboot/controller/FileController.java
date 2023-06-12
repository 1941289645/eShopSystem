package com.example.springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Files;
import com.example.springboot.entity.Members;
import com.example.springboot.entity.Products;
import com.example.springboot.mapper.FileMapper;
import com.example.springboot.mapper.MembersMapper;
import com.example.springboot.service.IMembersService;
import com.example.springboot.service.IProductsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 *  文件上传相关接口
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Value("${avatarimg.upload.path}")
    private String avartarPAth;

    @Value("${productimg.upload.path}")
    private String productPAth;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private IMembersService membersService;

    @Resource
    private MembersMapper membersMapper;

    @Resource
    private IProductsService productsService;

    /**
     * 文件上传接口
     * @param file 前端传递过来的文件
     * @return
     * @throws IOException
     */
    @PostMapping("upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename=file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        //定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID=uuid+ StrUtil.DOT+type;

        File uploadFile = new File(fileUploadPath + fileUUID);
        //判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile=uploadFile.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }

        String md5;
        String url;
        //上传文件到磁盘
        file.transferTo(uploadFile);
        //获取文件的md5，通过对比md5避免重复上传相同内容的文件
        md5= SecureUtil.md5(uploadFile);
        //从数据库查询是否存在相同的记录
        Files dbfiles = getFileByMd5(md5);
        if (dbfiles!=null){
            url=dbfiles.getUrl();
            //由于文件已存在,所以删除刚才上传的文件
            uploadFile.delete();
        }else{
            //数据库不存在重复文件，则不删除刚才上传的文件
            url="http://localhost:9090/file/"+fileUUID;
        }


        //存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);
        return url;
    }

    @PostMapping("/avartar/upload")
    public String  AvartarUpload(@RequestParam MultipartFile file,@RequestParam("id") String id) throws IOException {
        String orginalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(orginalFilename);

        //判断配置文件目录是否存在，若不存在则创建一个新的文件目录
        //定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUid = uuid + StrUtil.DOT +type;
        File uploadFile = new File(avartarPAth + fileUUid);

        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }

        //实现：对于相同内容不同文件名的文件，因为md5一样，在数据库中每个有一个记录，但是在磁盘中，只会存在一个最新的文件
        String url;
        //上传文件到磁盘
        file.transferTo(uploadFile);

        //把获取到的文件存储到磁盘目录
        url = "http://localhost:9090/file/avartar/"+fileUUid;


        //文件路径
        //存储数据库
        QueryWrapper<Members> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Members members = membersMapper.selectOne(queryWrapper);
        if(members == null){
            return null;
        }
        members.setAvatarurl(url);
        membersService.saveOrUpdate(members);
        return url; //文件下载链接
        //上传成功后返回url
    }



    @GetMapping("/avartar/{fileUUid}")
    public void downloadavartar(@PathVariable String fileUUid , HttpServletResponse response) throws IOException {

        //根据文件的唯一标识码获取文件
        File uploadFile = new File(avartarPAth + fileUUid);
        //文件上传路径
        //设置输出流格式
        ServletOutputStream os =response.getOutputStream();
        response.addHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileUUid,"UTF-8"));
        response.setContentType("application/octet-stream");

        //读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    @PostMapping("/product/upload")
    public String  productUpload(@RequestParam MultipartFile file) throws IOException {
        String orginalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(orginalFilename);

        //判断配置文件目录是否存在，若不存在则创建一个新的文件目录
        //定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUid = uuid + StrUtil.DOT +type;
        File uploadFile = new File(productPAth + fileUUid);

        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }

        //实现：对于相同内容不同文件名的文件，因为md5一样，在数据库中每个有一个记录，但是在磁盘中，只会存在一个最新的文件
        String url;
        //上传文件到磁盘
        file.transferTo(uploadFile);

        //把获取到的文件存储到磁盘目录
        url = "http://localhost:9090/file/product/"+fileUUid;


        //文件路径
        //存储数据库
//        Products products = productsService.getById(id);
//        if(products == null){
//            return null;
//        }
//        products.setImage(url);
//        productsService.saveOrUpdate(products);
        return url; //文件下载链接
        //上传成功后返回url
    }



    @GetMapping("/product/{fileUUid}")
    public void downloadproduct(@PathVariable String fileUUid , HttpServletResponse response) throws IOException {

        //根据文件的唯一标识码获取文件
        File uploadFile = new File(productPAth + fileUUid);
        //文件上传路径
        //设置输出流格式
        ServletOutputStream os =response.getOutputStream();
        response.addHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileUUid,"UTF-8"));
        response.setContentType("application/octet-stream");

        //读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    /**
     * 文件下载接口   http://localhost:9090/file/{fileUUID}
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        //根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        //设置输出流的格式
        ServletOutputStream os =response.getOutputStream();
        response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileUUID,"UTF-8"));
        response.setContentType("application/octet-stream");

        //读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }


    /**
     * 通过文件的md5查询文件
     * @param md5
     * @return
     */
    private Files getFileByMd5(String md5){
        //查询文件的md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5",md5);
        List<Files> fileList = fileMapper.selectList(queryWrapper);
        return fileList.size()==0?null : fileList.get(0);
    }

    //新增或更新
    @PostMapping("/update")
    public Result save(@RequestBody Files files){
        return Result.success(fileMapper.updateById(files));
    }


    @DeleteMapping("/{id}")
    public Result  delete(@PathVariable Integer id){
        Files files = fileMapper.selectById(id);
        files.setIsDelete(true);
        fileMapper.updateById(files);
        return Result.success();
    }

    //    批量删除接口
    @PostMapping("/del/batch")
    public Result  deleteBatch(@RequestBody List<Integer> ids){
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",ids);
        List<Files> files = fileMapper.selectList(queryWrapper);
        for(Files file : files){
            file.setIsDelete(true);
            fileMapper.updateById(file);
        }
        return Result.success();
    }



    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")  //接口路径,多条件查询
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name){
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        //查询未删除的记录
        queryWrapper.eq("is_delete",false);
        queryWrapper.orderByDesc("id");
        if(!"".equals(name)){
            queryWrapper.like("name",name);
        }
        return Result.success(fileMapper.selectPage(new Page<>(pageNum,pageSize),queryWrapper));
    }
}
