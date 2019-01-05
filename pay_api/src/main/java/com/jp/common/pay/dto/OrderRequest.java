package com.jp.common.pay.dto;

import com.jp.common.pay.domain.customized.OrderAO;
import com.jp.common.pay.domain.customized.OrderDetailAO;

import java.io.Serializable;
import java.util.List;

public class OrderRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单
     */
    private OrderAO order;

    /**
     * 订单商品信息
     */
    private List<OrderDetailAO> orderDetails;

    public OrderAO getOrder() {
        return order;
    }

    public void setOrder(OrderAO order) {
        this.order = order;
    }

    public List<OrderDetailAO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailAO> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
