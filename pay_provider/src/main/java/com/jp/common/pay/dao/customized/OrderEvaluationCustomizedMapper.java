package com.jp.common.pay.dao.customized;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author dingcc
 * @description
 * @date 2018/11/13 0013
 * @time 10:15
 */
public interface OrderEvaluationCustomizedMapper {
    /**
     * 获取简测数据提供情况
     * @param productId
     * @param userId
     * @return
     */
    List<Map<String,Object>> getDataProvideInfo(@Param("productId") String productId, @Param("userId") String userId);
}
