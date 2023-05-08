package com.example.springboot.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author lhc
 * @since 2023-05-07
 */
@Getter
@Setter
@ApiModel(value = "Products对象", description = "")
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer productCategoryId;

    private String name;

    private BigDecimal price;

    private LocalDate publishOn;

    private Boolean onSale;

    private String image;

    @TableField(exist = false)
    private String categoryName;

}
