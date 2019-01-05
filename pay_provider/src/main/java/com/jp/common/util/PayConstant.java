package com.jp.common.pay.util;

public class PayConstant {

    /**
     * 标价币种 默认人民币：CNY
     */
    public static final String FEE_TYPE_DEFAULT = "CNY";

    /**
     * 交易类型 小程序取值如下：JSAPI  APP--app支付 扫码支付--NATIVE
     */
    public static final String TRADE_TYPE_WX_APPLET = "JSAPI";

    public static final String TRADE_TYPE_APP = "APP";

    public static final String TRADE_TYPE_SCAN_CODE = "NATIVE";

    /**
     * 统一下单调用接口
     */
    public static final String PAY_ORDER_SUCCESS = "SUCCESS";

    public static final String PAY_ORDER_MSG = "OK";

    public static final String PAY_ORDER_FAIL = "FAIL";

    /**
     * 支付方式  1:微信  2：支付宝
     */
    public static final Integer PAY_WAY_WX = 1;
    public static final Integer PAY_WAY_ZFB = 2;

    /**
     * 支付结果
     */
    public static final Integer PAY_RESULT_SUCCESS = 1;
    public static final Integer PAY_RESULT_FAIL = 0;//失败

    /**
     * 下单
     */
    public static final String WX_PAY_BILL_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 微信服务器支付结果回调通知url
     */
    public static String WX_PAY_NOTIFY_URL = "https://applet.cnjpkj.com/wx-api/pay/wxPayBillCallback";

    /**
     * 商户平台API密钥
     */
    public static final String WX_PAY_PAY_KEY = "GURkraWQOG5rrQFeQdXuACLwsCEGievV";


    /**
     * 订单状态 0：删除 1：已完成 2：未付款  3：待发货 4：待收货 5：待评价 6:已取消
     */
    public static final Integer ORDER_STATUS_DEL = 0;
    public static final Integer ORDER_STATUS_FINISH = 1;
    public static final Integer ORDER_STATUS_NO_PAY = 2;
    public static final Integer ORDER_STATUS_WAIT_DELIVERY = 3;
    public static final Integer ORDER_STATUS_ALREADY_DELIVERY = 4;
    public static final Integer ORDER_STATUS_WAIT_EVALUATION = 5;
    public static final Integer ORDER_STATUS_CANCLE = 6;

    /**
     * 支付类型 1：购买 2：捐赠
     */
    public static final Integer PAY_TYPE_PURCHASE = 1;
    public static final Integer PAY_TYPE_DONATION = 2;

    /**
     * 购买类型 1：商品 2：简测项目 3：质检报告
     */
    public static final Integer PURCHASE_TYPE_PRODUCT = 1;
    public static final Integer PURCHASE_TYPE_PROJECT = 2;
    public static final Integer PURCHASE_TYPE_REPORT = 3;

}
