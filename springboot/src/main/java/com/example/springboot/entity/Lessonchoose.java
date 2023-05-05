package com.example.springboot.entity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lhc
 * @since 2023-04-18
 */
@Getter
@Setter
@ApiModel(value = "Lessonchoose对象", description = "")
public class Lessonchoose implements Serializable {

    private static final long serialVersionUID = 1L;

      private Integer snumber;

      private Integer lnumber;

      private Integer tnumber;

    private Integer psgrade;

    private Integer ksgrade;

    private Integer totalgrade;

    private Float gpa;

      private String semester;


}
