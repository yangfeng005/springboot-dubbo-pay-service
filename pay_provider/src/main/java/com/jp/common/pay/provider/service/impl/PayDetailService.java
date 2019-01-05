package com.jp.common.pay.provider.service.impl;


import com.jp.common.pay.dao.gen.PayDetailGeneratedMapper;
import com.jp.common.pay.domain.customized.PayDetailAO;
import com.jp.common.pay.dto.PayResultXMLEntity;
import com.jp.common.pay.entity.gen.PayDetailCriteria;
import com.jp.common.pay.service.IOrderService;
import com.jp.common.pay.service.IPayDetailService;
import com.jp.framework.common.model.ServiceResult;
import com.jp.framework.common.model.ServiceResultHelper;
import com.jp.framework.dao.BaseGeneratedMapper;
import com.jp.framework.service.AbstractBaseAOService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayDetailService extends AbstractBaseAOService<PayDetailAO, PayDetailCriteria> implements IPayDetailService {

    @Resource
    private PayDetailGeneratedMapper payDetailGeneratedMapper;

    @Resource
    private IOrderService orderService;


    @Override
    protected BaseGeneratedMapper<PayDetailAO, PayDetailCriteria> getGeneratedMapper() {
        return payDetailGeneratedMapper;
    }

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
    @Override
    public ServiceResult<Object> wxPaybill(String appId, String mchId, String orderNumber, String notifyUrl, String tradeType,
                                           String openid, BigDecimal totalFee, String body, String spbill_create_ip) {
        try {
            if (StringUtils.isEmpty(orderNumber)) {
                return ServiceResultHelper.genResultWithFaild("订单号不能为空", -1);
            }
            //生成的随机字符串
            String nonce_str = com.jp.common.pay.util.CharacterUtil.getRandomString(32);
            //组装参数，用户生成统一下单接口的签名
            Map<String, Object> packageParams = new HashMap<>();
            if (!StringUtils.isEmpty(openid)) {
                packageParams.put("openid", openid);
            }
            packageParams.put("appid", appId);
            packageParams.put("mch_id", mchId);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("body", body);
            packageParams.put("out_trade_no", orderNumber);//订单号
            packageParams.put("total_fee", String.valueOf(totalFee.intValue()));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            packageParams.put("spbill_create_ip", spbill_create_ip);
            packageParams.put("notify_url", notifyUrl);//支付成功后的回调地址
            packageParams.put("trade_type", tradeType);//支付方式

            String prestr = com.jp.common.pay.util.PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串

            //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
            String sign = com.jp.common.pay.util.PayUtil.sign(prestr, com.jp.common.pay.util.PayConstant.WX_PAY_PAY_KEY, "utf-8").toUpperCase();

            //拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
            packageParams.put("sign", sign);
            String xml = com.jp.common.pay.util.MapXmlUtil.map2Xmlstring(packageParams);
            LOG.info("调用统一下单接口请求XML数据：" + xml);

            //调用统一下单接口，并接受返回的结果
            String result = com.jp.common.pay.util.PayUtil.httpRequest(com.jp.common.pay.util.PayConstant.WX_PAY_BILL_URL, "POST", xml);

            LOG.info("调用统一下单接口返回XML数据：" + result);
            if (StringUtils.isEmpty(result)) {
                return ServiceResultHelper.genResultWithFaild("调用统一下单接口返回结果为空", -1);
            }

            // 将解析结果存储在HashMap中
            Map map = com.jp.common.pay.util.MapXmlUtil.xmlString2Map(result);
            String return_code = (String) map.get("return_code");//返回状态码
            Map<String, Object> data = new HashMap<>();//返回给小程序端需要的参数
            String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
            if (com.jp.common.pay.util.PayConstant.PAY_ORDER_SUCCESS.equals(return_code)) {
                //小程序支付
                if (!StringUtils.isEmpty(tradeType) && com.jp.common.pay.util.PayConstant.TRADE_TYPE_WX_APPLET.equals(tradeType)) {
                    data.put("appId", appId);
                    data.put("nonceStr", nonce_str);
                    data.put("package", "prepay_id=" + prepay_id);
                    data.put("signType", "MD5");
                    Long timeStamp = System.currentTimeMillis() / 1000;
                    data.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                    //拼接签名需要的参数
                    String stringSignTemp = com.jp.common.pay.util.PayUtil.createLinkString(data);
                    //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                    String paySign = com.jp.common.pay.util.PayUtil.sign(stringSignTemp, com.jp.common.pay.util.PayConstant.WX_PAY_PAY_KEY, "utf-8").toUpperCase();
                    data.put("paySign", paySign);
                    //app支付
                } else if (!StringUtils.isEmpty(tradeType) && com.jp.common.pay.util.PayConstant.TRADE_TYPE_APP.equals(tradeType)) {
                    //app的返回值
                    data.put("appid", appId);
                    data.put("partnerid", mchId);
                    data.put("prepayid", prepay_id);
                    data.put("noncestr", nonce_str);
                    Long timeStamp = System.currentTimeMillis() / 1000;
                    data.put("timestamp", timeStamp + "");
                    data.put("package", "Sign=WXPay");
                    String stringSignTemp = com.jp.common.pay.util.PayUtil.createLinkString(data);
                    String appSign = com.jp.common.pay.util.PayUtil.sign(stringSignTemp, com.jp.common.pay.util.PayConstant.WX_PAY_PAY_KEY, "utf-8").toUpperCase();
                    data.put("sign", appSign);
                    //扫码支付
                } else if (!StringUtils.isEmpty(tradeType) && com.jp.common.pay.util.PayConstant.TRADE_TYPE_SCAN_CODE.equals(tradeType)) {
                    //扫码支付的返回值
                    data.put("code_url", map.get("code_url"));
                }
                return ServiceResultHelper.genResultWithSuccess(data);
            }
            return ServiceResultHelper.genResultWithFaild(map.get("return_msg") != null ? map.get("return_msg").toString() : null, -1);
        } catch (Exception e) {
            LOG.info("下单异常：" + e.getMessage());
            return ServiceResultHelper.genResultWithFaild(e.getMessage(), -1);
        }
    }


    /**
     * 微信支付回调
     *
     * @param resultXMLEntity 微信支付结果异步推送XML数据包实体
     * @return
     */
    @Transactional
    @Override
    public String wxPayBillCallback(PayResultXMLEntity resultXMLEntity) {
        Map<String, Object> result = new HashMap<>();
        LOG.info("resultXMLEntity:" + resultXMLEntity.toString());
        if (resultXMLEntity == null) {
            LOG.error("微信支付结果异步推送XML数据包实体为空");
            result.put("return_code", com.jp.common.pay.util.PayConstant.PAY_ORDER_FAIL);
            result.put("return_msg", "微信支付结果异步推送XML数据包实体为空");
            return com.jp.common.pay.util.MapXmlUtil.map2Xmlstring(result);
        }
        if (!StringUtils.isEmpty(resultXMLEntity.getOutTradeNo())) {
            ServiceResult<PayDetailAO> payDetailRet = getPayDetailByOrderNumber(resultXMLEntity.getOutTradeNo());
            if (payDetailRet != null && payDetailRet.isSucceed() && payDetailRet.getData() != null) {
                //已经支付成功，再次通知则不做处理
                if (payDetailRet.getData().getPayStatus() != null && payDetailRet.getData().getPayStatus().equals(com.jp.common.pay.util.PayConstant.PAY_RESULT_SUCCESS)) {
                    result.put("return_code", com.jp.common.pay.util.PayConstant.PAY_ORDER_SUCCESS);
                    result.put("return_msg", com.jp.common.pay.util.PayConstant.PAY_ORDER_MSG);
                    return com.jp.common.pay.util.MapXmlUtil.map2Xmlstring(result);
                }
            } else {
                result.put("return_code", com.jp.common.pay.util.PayConstant.PAY_ORDER_FAIL);
                result.put("return_msg", "订单号不存在");
                return com.jp.common.pay.util.MapXmlUtil.map2Xmlstring(result);
            }
        }
        if (!StringUtils.isEmpty(resultXMLEntity.getReturnCode())) {
            //生成订单号
            PayDetailAO payDetail = new PayDetailAO();
            if (resultXMLEntity.getReturnCode().equals(com.jp.common.pay.util.PayConstant.PAY_ORDER_SUCCESS)) {
                //更新订单状态
                orderService.updateOrder(resultXMLEntity.getOutTradeNo(), com.jp.common.pay.util.PayConstant.ORDER_STATUS_WAIT_DELIVERY, resultXMLEntity.getTimeEnd());
                //更新支付状态
                payDetail.setPayStatus(com.jp.common.pay.util.PayConstant.PAY_RESULT_SUCCESS);
            } else if (resultXMLEntity.getReturnCode().equals(com.jp.common.pay.util.PayConstant.PAY_ORDER_FAIL)) {
                payDetail.setPayStatus(com.jp.common.pay.util.PayConstant.PAY_RESULT_FAIL);
            }
            payDetail.setSerialNumber(resultXMLEntity.getTransactionId());//支付流水号
            payDetail.setUpdateTime(!StringUtils.isEmpty(resultXMLEntity.getTimeEnd()) ?
                    com.jp.common.pay.util.DateUtil.parseStrToDate(resultXMLEntity.getTimeEnd(), com.jp.common.pay.util.DateUtil.DATE_FORMAT_6) : null);
            //更新支付信息
            PayDetailCriteria criteria = new PayDetailCriteria();
            criteria.createCriteria().andOrderNumberEqualTo(resultXMLEntity.getOutTradeNo());
            ServiceResult<Integer> payOrderRet = updateByCriteriaSelective(payDetail, criteria);
            if (payOrderRet != null && payOrderRet.isSucceed() && payOrderRet.getData() > 0) {
                LOG.info("微信支付成功");
                result.put("return_code", com.jp.common.pay.util.PayConstant.PAY_ORDER_SUCCESS);
                result.put("return_msg", com.jp.common.pay.util.PayConstant.PAY_ORDER_MSG);
                return com.jp.common.pay.util.MapXmlUtil.map2Xmlstring(result);
            }
        }
        result.put("return_code", com.jp.common.pay.util.PayConstant.PAY_ORDER_FAIL);
        result.put("return_msg", "微信支付结果通知处理失败");
        return com.jp.common.pay.util.MapXmlUtil.map2Xmlstring(result);
    }

    /**
     * 根据订单获取支付信息
     *
     * @param orderNumber
     * @return
     */
    public ServiceResult<PayDetailAO> getPayDetailByOrderNumber(String orderNumber) {
        PayDetailCriteria criteria = new PayDetailCriteria();
        criteria.createCriteria().andOrderNumberEqualTo(orderNumber);
        ServiceResult<List<PayDetailAO>> payDetailRet = selectByCriteria(criteria);
        if (payDetailRet != null && payDetailRet.isSucceed() && !CollectionUtils.isEmpty(payDetailRet.getData())) {
            return ServiceResultHelper.genResultWithSuccess(payDetailRet.getData().get(0));
        }
        return ServiceResultHelper.genResultWithFaild();
    }

}
