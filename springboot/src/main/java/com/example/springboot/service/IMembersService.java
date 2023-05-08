package com.example.springboot.service;

import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.entity.Members;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.entity.Teacher;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lhc
 * @since 2023-05-06
 */
public interface IMembersService extends IService<Members> {

    UserDTO login(UserDTO userDTO);

    Members register(UserDTO userDTO);

}
