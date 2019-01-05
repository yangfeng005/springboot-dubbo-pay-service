package com.jp.common.pay.provider.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jp.common.pay.dao.customized.OrderEvaluationCustomizedMapper;
import com.jp.common.pay.dao.gen.OrderEvaluationGeneratedMapper;
import com.jp.common.pay.domain.customized.OrderEvaluationAO;
import com.jp.common.pay.domain.customized.OrderEvaluationImageAO;
import com.jp.common.pay.entity.gen.OrderEvaluationCriteria;
import com.jp.common.pay.service.IOrderEvaluationImageService;
import com.jp.common.pay.service.IOrderEvaluationService;
import com.jp.framework.common.model.ServiceResult;
import com.jp.framework.common.model.ServiceResultHelper;
import com.jp.framework.common.util.Constant;
import com.jp.framework.common.util.Page;

import com.jp.framework.dao.BaseGeneratedMapper;
import com.jp.framework.service.AbstractBaseAOService;
import com.jp.zpzc.dto.req.BaseRequest;
import com.jp.zpzc.entity.customized.WxUserAO;
import com.jp.zpzc.service.IWxUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderEvaluationService extends AbstractBaseAOService<OrderEvaluationAO, OrderEvaluationCriteria> implements IOrderEvaluationService {

    @Resource
    private OrderEvaluationGeneratedMapper orderEvaluationGeneratedMapper;

    @Resource
    private IWxUserService wxUserService;
    @Resource
    private IOrderEvaluationImageService orderEvaluationImageService;
    @Resource
    private OrderEvaluationCustomizedMapper orderEvaluationCustomizedMapper;

    @Override
    protected BaseGeneratedMapper<OrderEvaluationAO, OrderEvaluationCriteria> getGeneratedMapper() {
        return orderEvaluationGeneratedMapper;
    }


    /**
     * 保存订单评价
     *
     * @param evaluation
     * @param userId
     * @param imageFileIds 评价图片id
     * @return
     */
    @Override
    @Transactional
    public ServiceResult<Boolean> save(OrderEvaluationAO evaluation, String[] imageFileIds, String userId) {
        if (evaluation == null) {
            return ServiceResultHelper.genResultWithFaild(Constant.ErrorCode.INVALID_PARAM_MSG, Constant.ErrorCode.INVALID_PARAM_CODE);
        }
        evaluation.setUserId(userId);
        evaluation.setCreateTime(new Date());
        ServiceResult<OrderEvaluationAO> evaluationRet = saveOrUpdateRetAO(evaluation);
        if (evaluationRet != null && evaluationRet.isSucceed() && evaluationRet.getData() != null) {
            //保存评价图片
            if (imageFileIds != null && imageFileIds.length > 0) {
                for (String imageFileId : imageFileIds) {
                    OrderEvaluationImageAO image = new OrderEvaluationImageAO();
                    image.setCreateTime(new Date());
                    image.setImageFileId(imageFileId);
                    image.setOrderEvaluationId(evaluationRet.getData().getId());
                    orderEvaluationImageService.saveOrUpdate(image);
                }
            }
            return ServiceResultHelper.genResultWithSuccess();
        }
        return ServiceResultHelper.genResultWithFaild();
    }

    /**
     * 查询评价
     *
     * @param productId
     * @return
     */
    @Override
    public ServiceResult<List<OrderEvaluationAO>> listEvaluation(BaseRequest request, String productId) {
        ServiceResult<List<OrderEvaluationAO>> ret = new ServiceResult<>();
        PageHelper.startPage(request.getPageNo(), request.getPageSize());
        OrderEvaluationCriteria criteria = new OrderEvaluationCriteria();
        criteria.createCriteria().andProductIdEqualTo(productId);
        ServiceResult<List<OrderEvaluationAO>> evaluationRet = selectByCriteria(criteria);
        if (evaluationRet != null && evaluationRet.isSucceed() && !CollectionUtils.isEmpty(evaluationRet.getData())) {
            evaluationRet.getData().forEach(evaluation -> {
                ServiceResult<WxUserAO> wxUserResult = wxUserService.selectByPrimaryKey(evaluation.getUserId());
                if (wxUserResult != null && wxUserResult.isSucceed() && wxUserResult.getData() != null) {
                    evaluation.setUser(wxUserResult.getData());
                }
                //获取评价的图片
                ServiceResult<List<OrderEvaluationImageAO>> imagesRet = orderEvaluationImageService.listByEvaluationId(evaluation.getId());
                if (imagesRet != null && imagesRet.isSucceed() && !CollectionUtils.isEmpty(imagesRet.getData())) {
                    evaluation.setImages(imagesRet.getData());
                }
                //提供简测数据情况
                List<Map<String, Object>> provideInfoList = orderEvaluationCustomizedMapper.getDataProvideInfo(productId, evaluation.getUserId());
                if (!CollectionUtils.isEmpty(provideInfoList)) {
                    evaluation.setDataProvideStatus(true);
                    evaluation.setProvideTime((Date) provideInfoList.get(0).get("create_time"));
                } else {
                    evaluation.setDataProvideStatus(false);
                }
            });
        }
        ret.setData(evaluationRet.getData());
        ret.setSucceed(true);
        ret.setAdditionalProperties("page", Page.obtainPage(new PageInfo<>(evaluationRet.getData())));
        return ret;

    }

}
