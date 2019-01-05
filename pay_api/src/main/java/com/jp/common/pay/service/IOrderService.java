package com.jp.common.pay.service;

import com.jp.common.pay.domain.customized.OrderAO;
import com.jp.common.pay.dto.OrderQueryRequest;
import com.jp.common.pay.dto.ProductPayRes;
import com.jp.common.pay.entity.gen.OrderCriteria;
import com.jp.framework.common.model.ServiceResult;
import com.jp.framework.service.IBaseAOService;

import java.util.List;

public interface IOrderService extends IBaseAOService<OrderAO, OrderCriteria> {

    /**
     * 更新订单信息
     *
     * @param orderNumber 订单号
     * @param orderStatus 订单状态
     * @param timeEnd     支付完成时间
     * @return
     */
    ServiceResult<Integer> updateOrder(String orderNumber, Integer orderStatus, String timeEnd);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber 订单号
     * @return
     */
    ServiceResult<OrderAO> getOrderByOrderNumber(String orderNumber);

    /**
     * 根据用户ID、订单状态查询订单
     *
     * @param userId      用户ID
     * @param req 订单查询请求
     * @return
     */
    ServiceResult<List<OrderAO>> getOrderByUserId(String userId, OrderQueryRequest req);

    /**
     * 获取产品捐赠信息
     *
     * @param productId
     * @param purchaseType 购买类型 1：商品  2：简测项目 3：质检报告
     * @return
     */
    ServiceResult<ProductPayRes> getDonateInfoOfProduct(String productId, Integer purchaseType);

}
