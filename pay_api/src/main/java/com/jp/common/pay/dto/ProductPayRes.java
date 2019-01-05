package com.jp.common.pay.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 产品支付信息返回
 *
 * @author yangfeng
 * @date 2018.10.11
 */
public class ProductPayRes implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品id
     */
    private String productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品图片id
     */
    private String productFileId;

    /**
     * 支付人数
     */
    private Integer userCount;

    /**
     * 支付总金额
     */
    private BigDecimal payMoneyCount;


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductFileId() {
        return productFileId;
    }

    public void setProductFileId(String productFileId) {
        this.productFileId = productFileId;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public BigDecimal getPayMoneyCount() {
        return payMoneyCount;
    }

    public void setPayMoneyCount(BigDecimal payMoneyCount) {
        this.payMoneyCount = payMoneyCount;
    }
}
