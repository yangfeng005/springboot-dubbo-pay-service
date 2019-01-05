package com.jp.common.pay.service;

import com.jp.common.pay.dto.OrderRequest;
import com.jp.framework.common.model.ServiceResult;

public interface IPayService {


    /**
     * 付款
     *
     * @param orderRequest
     * @param userId
     * @param openid           用户openid
     * @param body             商品描述信息
     * @param tradeType        交易类型 小程序取值如下：JSAPI  APP--app支付
     * @param payType          支付类型 1：购买 2：捐赠
     * @param purchaseType     购买类型 1：商品 2：项目
     * @param spbill_create_ip 支付客户端ip
     * @return
     */
    ServiceResult<Object> payment(OrderRequest orderRequest, String userId, String openid, String body, String tradeType, Integer payType, Integer purchaseType, String spbill_create_ip, String orderNumber);

}
