package com.jp.common.pay.provider.service.impl;


import com.jp.common.pay.domain.customized.OrderAO;
import com.jp.common.pay.domain.customized.PayDetailAO;
import com.jp.common.pay.dto.OrderRequest;
import com.jp.common.pay.service.IOrderDetailService;
import com.jp.common.pay.service.IOrderService;
import com.jp.common.pay.service.IPayDetailService;
import com.jp.common.pay.service.IPayService;
import com.jp.common.pay.util.GenerateNum;
import com.jp.common.pay.util.PayConstant;
import com.jp.framework.common.model.ServiceResult;
import com.jp.framework.common.model.ServiceResultHelper;

import com.jp.framework.common.util.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class PayService implements IPayService {


    /**
     * 小程序appid
     */
    @Value("${wx.applet.appid}")
    String wx_applet_appid;

    /**
     * APP移动应用端appid
     */
    @Value("${app.appid}")
    String app_appid;
    /**
     * 微信小程序商户平台商户号
     */
    @Value("${wx.sh.mch_id}")
    String mchId;

    /**
     * 微信app商户平台商户号
     */
    @Value("${wx.app.mch_id}")
    String appMchId;


    @Resource
    private IOrderService orderService;

    @Resource
    private IOrderDetailService orderDetailService;

    @Resource
    private IPayDetailService payDetailService;

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
    @Transactional
    @Override
    public ServiceResult<Object> payment(OrderRequest orderRequest, String userId, String openid, String body, String tradeType, Integer payType, Integer purchaseType, String spbill_create_ip, String orderNumber) {
        if (orderRequest == null || orderRequest.getOrder() == null || CollectionUtils.isEmpty(orderRequest.getOrderDetails()) || StringUtils.isEmpty(tradeType)) {
            return ServiceResultHelper.genResultWithFaild(Constant.ErrorCode.INVALID_PARAM_MSG, Constant.ErrorCode.INVALID_PARAM_CODE);
        }
        OrderAO order = null;
        //没有下单
        if (StringUtils.isEmpty(orderNumber)) {
            //生成订单号
            orderNumber = GenerateNum.getInstance().GenerateOrder();
            order = orderRequest.getOrder();
            order.setOrderNumber(orderNumber);
            order.setOrderTime(new Date());
            order.setPayType(payType == null ? PayConstant.PAY_TYPE_PURCHASE : payType);
            order.setPurchaseType(purchaseType == null ? PayConstant.PURCHASE_TYPE_PRODUCT : purchaseType);
            order.setOrderStatus(PayConstant.ORDER_STATUS_NO_PAY);
            order.setUserId(userId);
            ServiceResult<Boolean> orderRet = orderService.saveOrUpdate(order);
            if (orderRet != null && orderRet.isSucceed()) {
                String finalOrderNumber = orderNumber;
                orderRequest.getOrderDetails().forEach(orderDetail -> {
                    orderDetail.setOrderNumber(finalOrderNumber);
                    orderDetailService.saveOrUpdate(orderDetail);
                });
                //支付信息
                PayDetailAO payDetail = new PayDetailAO();
                payDetail.setCreateTime(new Date());
                payDetail.setPayPlatform(orderRequest.getOrder().getPayWay());
                payDetail.setOrderNumber(orderNumber);
                payDetail.setTradeType(tradeType);
                payDetailService.saveOrUpdate(payDetail);
            }
        }else {
            //已经下单但取消支付
            ServiceResult<OrderAO> orderResult = orderService.getOrderByOrderNumber(orderNumber);
            if (orderResult != null && orderResult.isSucceed() && orderResult.getData() != null) {
                order = orderResult.getData();
            }
        }

        //微信支付
        if (PayConstant.PAY_WAY_WX.equals(orderRequest.getOrder().getPayWay())) {
            //微信支付调用统一下单接口
            //小程序端
            if (!StringUtils.isEmpty(tradeType) && PayConstant.TRADE_TYPE_WX_APPLET.equals(tradeType)) {
                return payDetailService.wxPaybill(wx_applet_appid, mchId, orderNumber, PayConstant.WX_PAY_NOTIFY_URL, PayConstant.TRADE_TYPE_WX_APPLET,
                        openid, order.getActuallyPayMoney(), body, spbill_create_ip);
                //app端
            } else if (!StringUtils.isEmpty(tradeType) && PayConstant.TRADE_TYPE_APP.equals(tradeType)) {
                return payDetailService.wxPaybill(app_appid, appMchId, orderNumber, PayConstant.WX_PAY_NOTIFY_URL, PayConstant.TRADE_TYPE_APP,
                        null, order.getActuallyPayMoney(), body, spbill_create_ip);
                //扫码支付
            } else if (!StringUtils.isEmpty(tradeType) && PayConstant.TRADE_TYPE_SCAN_CODE.equals(tradeType)) {
                return payDetailService.wxPaybill(wx_applet_appid, mchId, orderNumber, PayConstant.WX_PAY_NOTIFY_URL, PayConstant.TRADE_TYPE_SCAN_CODE,
                        openid, order.getActuallyPayMoney(), body, spbill_create_ip);
            }
        }
        //TODO 支付宝支付
        return ServiceResultHelper.genResultWithFaild();
    }

}
