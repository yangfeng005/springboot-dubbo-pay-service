package com.jp.common.pay.provider.service.impl;



import com.jp.common.pay.dao.customized.OrderDetailCustomizedMapper;
import com.jp.common.pay.dao.gen.OrderDetailGeneratedMapper;
import com.jp.common.pay.domain.customized.OrderDetailAO;
import com.jp.common.pay.entity.gen.OrderDetailCriteria;
import com.jp.common.pay.service.IOrderDetailService;
import com.jp.common.pay.util.PayConstant;
import com.jp.framework.common.model.ServiceResult;
import com.jp.framework.common.model.ServiceResultHelper;
import com.jp.framework.common.util.Constant;
import com.jp.framework.dao.BaseGeneratedMapper;
import com.jp.framework.service.AbstractBaseAOService;
import com.jp.zpzc.entity.customized.ProductAO;
import com.jp.zpzc.service.IProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class OrderDetailService extends AbstractBaseAOService<OrderDetailAO, OrderDetailCriteria> implements IOrderDetailService {

    @Resource
    private OrderDetailGeneratedMapper orderDetailGeneratedMapper;
    @Resource
    private IProductService productService;
    @Resource
    private OrderDetailCustomizedMapper orderDetailCustomizedMapper;


    @Override
    protected BaseGeneratedMapper<OrderDetailAO, OrderDetailCriteria> getGeneratedMapper() {
        return orderDetailGeneratedMapper;
    }


    /**
     * 根据订单号查询订单详情
     *
     * @param orderNumber 订单号
     * @return
     */
    @Override
    public ServiceResult<List<OrderDetailAO>> getOrderDetailByOrderNumber(String orderNumber, Integer purchaseType) {
        if (orderNumber == null) {
            return ServiceResultHelper.genResultWithFaild(Constant.ErrorCode.INVALID_PARAM_MSG, Constant.ErrorCode.INVALID_PARAM_CODE);
        }
        OrderDetailCriteria criteria = new OrderDetailCriteria();
        criteria.createCriteria().andOrderNumberEqualTo(orderNumber);
        ServiceResult<List<OrderDetailAO>> orderDetailResult = selectByCriteria(criteria);
        if (orderDetailResult != null && orderDetailResult.isSucceed() && !CollectionUtils.isEmpty(orderDetailResult.getData())) {
            List<OrderDetailAO> orderDetailList = orderDetailResult.getData();
            if (PayConstant.PURCHASE_TYPE_PRODUCT.equals(purchaseType)) {
                orderDetailList.forEach(orderDetail -> {
                    // 根据商品ID查询商品信息
                    ServiceResult<ProductAO> productResult = productService.selectByPrimaryKey(orderDetail.getProductId());
                    if (productResult != null && productResult.isSucceed() && productResult.getData() != null) {
                        ProductAO product = productResult.getData();
                        orderDetail.setProductName(product.getProductName());
                        orderDetail.setProductFileId(product.getProductFileId());
                    }
                });
            } else if (PayConstant.PURCHASE_TYPE_PROJECT.equals(purchaseType)) {
                orderDetailList.forEach(orderDetail -> {
                    // 根据项目ID查询简测项目信息
                    Map<String, Object> map = orderDetailCustomizedMapper.getDetectProjectInfo(orderDetail.getProductId());
                    if (map != null) {
                        orderDetail.setProductName((String) map.get("project_name"));
                        orderDetail.setProductFileId((String) map.get("product_thumbnail"));
                    }
                });
            }
        }
        return orderDetailResult;
    }
}
