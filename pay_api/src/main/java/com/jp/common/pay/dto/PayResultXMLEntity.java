package com.jp.common.pay.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信支付结果异步推送XML数据包实体
 */

@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class PayResultXMLEntity {

    // 返回状态码
    @XmlElement(name = "return_code")
    protected String returnCode;

    // 返回信息
    @XmlElement(name = "return_msg")
    protected String returnMsg;

    // 业务结果
    @XmlElement(name = "result_code")
    protected String resultCode;

    // 商户订单号
    @XmlElement(name = "out_trade_no")
    protected String outTradeNo;

    // 支付完成时间
    @XmlElement(name = "time_end")
    protected String timeEnd;

    // 微信支付订单号
    @XmlElement(name = "transaction_id")
    protected String transactionId;

    // 微信支付订单号
    @XmlElement(name = "total_fee")
    protected String totalFee;

    // 微信支付订单号
    @XmlElement(name = "mch_id")
    protected String mchId;

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    @Override
    public String toString() {
        return "PayResultXMLEntity{" +
                "returnCode='" + returnCode + '\'' +
                ", returnMsg='" + returnMsg + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", totalFee='" + totalFee + '\'' +
                ", mchId='" + mchId + '\'' +
                '}';
    }
}


