package com.jp.common.pay.domain.customized;

import com.jp.common.pay.entity.gen.Order;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import java.io.Serializable;
import java.util.List;

/**
 * 应用对象 - Order.
 * <p>
 * 该类于 2018-09-27 10:53:19 首次生成，后由开发手工维护。
 * </p>
 * @author yangfeng
 * @version 1.0.0, Sep 27, 2018
 */
@JsonSerialize(include = Inclusion.ALWAYS)
public final class OrderAO extends Order implements Serializable {

    /**
     * 默认的序列化 id.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 订单详情列表
     */
    private List<OrderDetailAO> orderDetails;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public List<OrderDetailAO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailAO> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
