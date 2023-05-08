package com.example.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Constants;
import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.entity.Members;
import com.example.springboot.entity.Teacher;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.MembersMapper;
import com.example.springboot.service.IMembersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.utils.TokenUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lhc
 * @since 2023-05-06
 */
@Service
public class MembersServiceImpl extends ServiceImpl<MembersMapper, Members> implements IMembersService {

    private static final Log LOG= Log.get();

    @Override
    public UserDTO login(UserDTO userDTO) {
        Members one = getUserInfo(userDTO);
        if(one !=null){
            BeanUtil.copyProperties(one,userDTO,true);
            //设置token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDTO.setToken(token);
            return userDTO;
        }else {
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }
    }

    @Override
    public Members register(UserDTO userDTO) {
        Members one = getUserInfo(userDTO);
        if(one==null){
            one=new Members();
            BeanUtil.copyProperties(userDTO,one,true);
            save(one);   // 把copy完之后的对象存储到数据库
        }else{
            throw new ServiceException(Constants.CODE_600,"用户已存在");
        }
        return one;
    }

    private Members getUserInfo(UserDTO userDTO){
        QueryWrapper<Members> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("login",userDTO.getLogin());
        queryWrapper.eq("password",userDTO.getPassword());
        Members one;
        try{
            one = getOne(queryWrapper);  //从数据库查询用户信息
        }catch (Exception e){
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return one;
    }

}
