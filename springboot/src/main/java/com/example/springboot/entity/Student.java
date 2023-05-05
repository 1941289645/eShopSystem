package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author lhc
 * @since 2023-04-15
 */
@Getter
@Setter
  @ApiModel(value = "Student对象", description = "")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Integer studentid;

    private String name;

    private String sex;

    private LocalDate dataofbirth;

    private String mobilephone;

    private String status;

    private String logn;

    private String pswd;

    private String sprofession;

    private Integer age;

    private String nativeplace;

    private String sdept;

    private Integer deptid;

    private String avatarurl;


}
