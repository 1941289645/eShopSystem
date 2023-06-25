package com.example.springboot.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2023-06-22
 */
@Getter
@Setter
  @ApiModel(value = "Orders对象", description = "")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "auto_id", type = IdType.AUTO)
    private Integer autoId;

      private String orderId;

    private Integer memberId;

    private String contactName;

    private String contactPhoneno;

    private String contactAddress;

    private BigDecimal money;

    private String paymentMode;

    private LocalDateTime orderDate;

    @ApiModelProperty("付款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    @ApiModelProperty("付款编号")
    private String payno;

    private Integer status;

    private String expressNumber;

    @TableField(exist = false)
    private List<Orderdetails> orderdetails;
}
