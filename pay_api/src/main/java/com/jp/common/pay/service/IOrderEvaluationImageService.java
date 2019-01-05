package com.jp.common.pay.service;

import com.jp.common.pay.entity.gen.OrderEvaluationImageCriteria;
import com.jp.framework.common.model.ServiceResult;
import com.jp.framework.service.IBaseAOService;
import com.jp.common.pay.domain.customized.OrderEvaluationImageAO;


import java.util.List;

public interface IOrderEvaluationImageService extends IBaseAOService<OrderEvaluationImageAO, OrderEvaluationImageCriteria> {


    /**
     * 根据评价ID查询图片
     *
     * @param evaluationId
     * @return
     */
    ServiceResult<List<OrderEvaluationImageAO>> listByEvaluationId(String evaluationId);


}
