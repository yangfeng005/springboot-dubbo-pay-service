package com.jp.common.pay.dto;


import com.jp.zpzc.dto.req.BaseRequest;

/**
 * @author dingcc
 * @desciption 订单查询请求
 * @date 2018/10/12 0012
 * @time 14:07
 */
public class OrderQueryRequest extends BaseRequest {

    /**
     * 订单状态
     */
    private Integer orderStatus;

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }
}
