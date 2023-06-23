package com.example.springboot.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private LocalDateTime payTime;

    @ApiModelProperty("付款编号")
    private String payno;

    private Integer status;


}
