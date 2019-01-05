package com.jp.common.pay.provider.service.impl;


import com.jp.common.pay.dao.gen.OrderEvaluationImageGeneratedMapper;
import com.jp.common.pay.domain.customized.OrderEvaluationImageAO;
import com.jp.common.pay.entity.gen.OrderEvaluationImageCriteria;
import com.jp.common.pay.service.IOrderEvaluationImageService;
import com.jp.framework.common.model.ServiceResult;
import com.jp.framework.dao.BaseGeneratedMapper;
import com.jp.framework.service.AbstractBaseAOService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderEvaluationImageService extends AbstractBaseAOService<OrderEvaluationImageAO, OrderEvaluationImageCriteria> implements IOrderEvaluationImageService {

    @Resource
    private OrderEvaluationImageGeneratedMapper orderEvaluationImageGeneratedMapper;


    @Override
    protected BaseGeneratedMapper<OrderEvaluationImageAO, OrderEvaluationImageCriteria> getGeneratedMapper() {
        return orderEvaluationImageGeneratedMapper;
    }

    /**
     * 根据评价ID查询图片
     *
     * @param evaluationId
     * @return
     */
    @Override
    public ServiceResult<List<OrderEvaluationImageAO>> listByEvaluationId(String evaluationId) {
        OrderEvaluationImageCriteria criteria = new OrderEvaluationImageCriteria();
        criteria.createCriteria().andOrderEvaluationIdEqualTo(evaluationId);
        return selectByCriteria(criteria);
    }


}
