package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.entity.Teacher;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lhc
 * @since 2023-04-15
 */
public interface ITeacherService extends IService<Teacher> {

     UserDTO login(UserDTO userDTO);

     Teacher register(UserDTO userDTO);
}
