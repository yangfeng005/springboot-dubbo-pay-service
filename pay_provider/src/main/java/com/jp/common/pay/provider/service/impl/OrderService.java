package com.jp.common.pay.provider.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jp.common.pay.dao.customized.OrderCustomizedMapper;
import com.jp.common.pay.dao.gen.OrderGeneratedMapper;
import com.jp.common.pay.domain.customized.OrderAO;
import com.jp.common.pay.domain.customized.OrderDetailAO;
import com.jp.common.pay.dto.OrderQueryRequest;
import com.jp.common.pay.dto.ProductPayRes;
import com.jp.common.pay.entity.gen.OrderCriteria;
import com.jp.common.pay.service.IOrderDetailService;
import com.jp.common.pay.service.IOrderService;
import com.jp.common.pay.util.DateUtil;
import com.jp.common.pay.util.PayConstant;
import com.jp.framework.common.model.ServiceResult;
import com.jp.framework.common.model.ServiceResultHelper;
import com.jp.framework.common.util.Constant;
import com.jp.framework.common.util.Page;

import com.jp.framework.dao.BaseGeneratedMapper;
import com.jp.framework.service.AbstractBaseAOService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderService extends AbstractBaseAOService<OrderAO, OrderCriteria> implements IOrderService {

    @Resource
    private OrderGeneratedMapper orderGeneratedMapper;
    @Resource
    private IOrderDetailService orderDetailService;
    @Resource
    private OrderCustomizedMapper orderCustomizedMapper;


    @Override
    protected BaseGeneratedMapper<OrderAO, OrderCriteria> getGeneratedMapper() {
        return orderGeneratedMapper;
    }


    /**
     * 更新订单信息
     *
     * @param orderNumber 订单号
     * @param orderStatus 订单状态
     * @param timeEnd     支付完成时间
     * @return
     */
    @Override
    public ServiceResult<Integer> updateOrder(String orderNumber, Integer orderStatus, String timeEnd) {
        OrderCriteria criteria = new OrderCriteria();
        criteria.createCriteria().andOrderNumberEqualTo(orderNumber);
        OrderAO order = new OrderAO();
        order.setOrderStatus(orderStatus);
        if (!StringUtils.isEmpty(timeEnd)) {
            order.setPayTime(DateUtil.parseStrToDate(timeEnd, DateUtil.DATE_FORMAT_6));
        }
        return updateByCriteriaSelective(order, criteria);
    }

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber 订单号
     * @return
     */
    @Override
    public ServiceResult<OrderAO> getOrderByOrderNumber(String orderNumber) {
        if (orderNumber == null) {
            return ServiceResultHelper.genResultWithFaild(Constant.ErrorCode.INVALID_PARAM_MSG, Constant.ErrorCode.INVALID_PARAM_CODE);
        }
        ServiceResult<OrderAO> result = new ServiceResult<>();
        OrderCriteria criteria = new OrderCriteria();
        criteria.createCriteria().andOrderNumberEqualTo(orderNumber);
        ServiceResult<List<OrderAO>> orderResult = selectByCriteria(criteria);
        if (orderResult != null && orderResult.isSucceed() && !CollectionUtils.isEmpty(orderResult.getData())) {
            OrderAO order = orderResult.getData().get(0);
            ServiceResult<List<OrderDetailAO>> orderDetailResult = orderDetailService.getOrderDetailByOrderNumber(orderNumber, order.getPurchaseType());
            if (orderDetailResult != null && orderDetailResult.isSucceed() && !CollectionUtils.isEmpty(orderDetailResult.getData())) {
                order.setOrderDetails(orderDetailResult.getData());
            }
            result.setData(order);
            result.setSucceed(true);
        }
        return result;
    }

    /**
     * 根据用户ID、订单状态查询订单
     *
     * @param userId      用户ID
     * @param req         订单查询请求
     * @return
     */
    @Override
    public ServiceResult<List<OrderAO>> getOrderByUserId(String userId, OrderQueryRequest req) {
        OrderCriteria criteria = new OrderCriteria();
        OrderCriteria.Criteria c = criteria.createCriteria();
        c.andUserIdEqualTo(userId);
        Integer orderStatus = req.getOrderStatus();
        if (orderStatus != null) {
            c.andOrderStatusEqualTo(orderStatus);
        }
        c.andPayTypeEqualTo(PayConstant.PAY_TYPE_PURCHASE);  // 订单查询购买的
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        ServiceResult<List<OrderAO>> orderResult = selectByCriteria(criteria);
        if (orderResult != null && orderResult.isSucceed() && !CollectionUtils.isEmpty(orderResult.getData())) {
            List<OrderAO> orderList = orderResult.getData();
            orderList.forEach(order -> {
                // 根据订单号查询订单详情
                ServiceResult<List<OrderDetailAO>> orderDetailResult = orderDetailService.getOrderDetailByOrderNumber(order.getOrderNumber(), order.getPurchaseType());
                if (orderDetailResult != null && orderDetailResult.isSucceed() && !CollectionUtils.isEmpty(orderDetailResult.getData())) {
                    order.setOrderDetails(orderDetailResult.getData());
                }
            });
        }
        orderResult.setAdditionalProperties("page", Page.obtainPage(new PageInfo(orderResult != null ? orderResult.getData() : null)));
        return orderResult;
    }

    /**
     * 获取产品捐赠信息
     *
     * @param productId
     * @param purchaseType
     * @return
     */
    @Override
    public ServiceResult<ProductPayRes> getDonateInfoOfProduct(String productId, Integer purchaseType) {
        ProductPayRes productPayRes = null;
        if (PayConstant.PURCHASE_TYPE_PRODUCT.equals(purchaseType)) {
            // 产品捐赠
            productPayRes = orderCustomizedMapper.getDonateInfoOfProduct(productId, PayConstant.PAY_TYPE_DONATION, purchaseType);
        } else if (PayConstant.PURCHASE_TYPE_REPORT.equals(purchaseType)) {
            // 报告打赏
            productPayRes = orderCustomizedMapper.getDonateInfoOfReport(productId, PayConstant.PAY_TYPE_DONATION, purchaseType);
        }
        return ServiceResultHelper.genResultWithSuccess(productPayRes);
    }
}
