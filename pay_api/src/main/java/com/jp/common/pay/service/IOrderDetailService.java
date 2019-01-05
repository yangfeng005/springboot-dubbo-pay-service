package com.jp.common.pay.service;

import com.jp.common.pay.domain.customized.OrderDetailAO;
import com.jp.common.pay.entity.gen.OrderDetailCriteria;
import com.jp.framework.common.model.ServiceResult;
import com.jp.framework.service.IBaseAOService;

import java.util.List;

public interface IOrderDetailService extends IBaseAOService<OrderDetailAO, OrderDetailCriteria> {

    /**
     * 根据订单号查询订单详情
     * @param orderNumber 订单号
     * @return
     */
    ServiceResult<List<OrderDetailAO>> getOrderDetailByOrderNumber(String orderNumber, Integer purchaseType);
}
