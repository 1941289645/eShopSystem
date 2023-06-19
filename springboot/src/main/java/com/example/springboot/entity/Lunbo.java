package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 前台轮播图
 * </p>
 *
 * @author lhc
 * @since 2023-06-12
 */
@Getter
@Setter
  @ApiModel(value = "Lunbo对象", description = "前台轮播图")
public class Lunbo implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("图片")
      private String image;

      @ApiModelProperty("商品链接")
      private String link;

      @ApiModelProperty("商品id")
      private Integer productId;

    private String productName;


}
