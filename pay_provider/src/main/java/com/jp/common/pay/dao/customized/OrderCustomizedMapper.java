package com.jp.common.pay.dao.customized;

import com.jp.common.pay.dto.ProductPayRes;
import org.apache.ibatis.annotations.Param;


public interface OrderCustomizedMapper {


    /**
     * 获取产品捐赠信息
     *
     * @param productId
     * @param payType   支付类型 1：购买 2：捐赠
     * @param purchaseType 购买类型 1：商品  2：简测项目 3：质检报告
     * @return
     */
    ProductPayRes getDonateInfoOfProduct(@Param("productId") String productId, @Param("payType") Integer payType,
                                         @Param("purchaseType") Integer purchaseType);

    /**
     * 获取报告打赏信息
     * @param productId
     * @param payType
     * @return
     */
    ProductPayRes getDonateInfoOfReport(@Param("productId") String productId, @Param("payType") Integer payType,
                                        @Param("purchaseType") Integer purchaseType);
}
