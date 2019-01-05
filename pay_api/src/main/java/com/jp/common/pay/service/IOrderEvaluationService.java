package com.jp.common.pay.service;

import com.jp.common.pay.domain.customized.OrderEvaluationAO;
import com.jp.common.pay.entity.gen.OrderEvaluationCriteria;
import com.jp.framework.common.model.ServiceResult;
import com.jp.framework.service.IBaseAOService;
import com.jp.zpzc.dto.req.BaseRequest;

import java.util.List;

public interface IOrderEvaluationService extends IBaseAOService<OrderEvaluationAO, OrderEvaluationCriteria> {


    /**
     * 保存订单项目评价
     *
     * @param evaluation
     * @param imageFileIds
     * @param userId
     * @return
     */
    ServiceResult<Boolean> save(OrderEvaluationAO evaluation, String[] imageFileIds, String userId);

    /**
     * 查询评价
     *
     * @param productId
     * @return
     */
    ServiceResult<List<OrderEvaluationAO>> listEvaluation(BaseRequest request, String productId);

}
