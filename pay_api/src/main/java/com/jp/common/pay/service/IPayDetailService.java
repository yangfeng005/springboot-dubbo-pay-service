package com.jp.common.pay.service;

import com.jp.common.pay.domain.customized.PayDetailAO;
import com.jp.common.pay.dto.PayResultXMLEntity;
import com.jp.common.pay.entity.gen.PayDetailCriteria;
import com.jp.framework.common.model.ServiceResult;
import com.jp.framework.service.IBaseAOService;

import java.math.BigDecimal;

public interface IPayDetailService extends IBaseAOService<PayDetailAO, PayDetailCriteria> {


    /**
     * 微信下单
     *
     * @param appId            APP端或者小程序appid
     * @param mchId            商户号
     * @param orderNumber      订单编号
     * @param notifyUrl        通知url
     * @param tradeType        交易类型
     * @param openid           用户标识
     * @param totalFee         订单总金额，单位为分
     * @param body             商品描述
     * @param spbill_create_ip APP和网页支付提交用户端ip
     * @return
     */
    ServiceResult<Object> wxPaybill(String appId, String mchId, String orderNumber, String notifyUrl,
                                    String tradeType, String openid, BigDecimal totalFee, String body, String spbill_create_ip);


    /**
     * 微信支付回调
     *
     * @param resultXMLEntity 微信支付结果异步推送XML数据包实体
     * @return
     */
    String wxPayBillCallback(PayResultXMLEntity resultXMLEntity);

    /**
     * 根据订单获取支付信息
     * @param orderNumber
     * @return
     */
    ServiceResult<PayDetailAO> getPayDetailByOrderNumber(String orderNumber);
}
