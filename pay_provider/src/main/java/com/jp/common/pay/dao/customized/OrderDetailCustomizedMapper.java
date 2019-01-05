package com.jp.common.pay.dao.customized;

import java.util.Map;

/**
 * @author dingcc
 * @description 订单详情自定义数据接口
 * @date 2018/11/15 0015
 * @time 15:51
 */
public interface OrderDetailCustomizedMapper {

    /**
     * 获取简测项目信息
     * @param productId
     * @return
     */
    Map<String,Object> getDetectProjectInfo(String productId);
}
