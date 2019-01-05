package com.jp.common.pay.domain.customized;

import com.jp.common.pay.entity.gen.OrderEvaluation;
import com.jp.zpzc.entity.customized.WxUserAO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 应用对象 - OrderEvaluation.
 * <p>
 * 该类于 2018-10-12 15:29:28 首次生成，后由开发手工维护。
 * </p>
 *
 * @author yangfeng
 * @version 1.0.0, Oct 12, 2018
 */
@JsonSerialize(include = Inclusion.ALWAYS)
public final class OrderEvaluationAO extends OrderEvaluation implements Serializable {

    /**
     * 默认的序列化 id.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 评价人
     */
    private WxUserAO user;

    /**
     * 评价图片
     */
    private List<OrderEvaluationImageAO> images;

    /**
     * 商品缩略图
     */
    private String productThumbnail;
    /**
     * 商品名称
     */
    private String projectName;
    /**
     * 销售价
     */
    private BigDecimal sellPrice;
    /**
     * 简测数据提供状态
     */
    private Boolean dataProvideStatus;
    /**
     * 简测数据提供时间
     */
    private Date provideTime;

    public Boolean getDataProvideStatus() {
        return dataProvideStatus;
    }

    public void setDataProvideStatus(Boolean dataProvideStatus) {
        this.dataProvideStatus = dataProvideStatus;
    }

    public Date getProvideTime() {
        return provideTime;
    }

    public void setProvideTime(Date provideTime) {
        this.provideTime = provideTime;
    }

    public List<OrderEvaluationImageAO> getImages() {
        return images;
    }

    public void setImages(List<OrderEvaluationImageAO> images) {
        this.images = images;
    }

    public WxUserAO getUser() {
        return user;
    }

    public void setUser(WxUserAO user) {
        this.user = user;
    }

    public String getProductThumbnail() {
        return productThumbnail;
    }

    public void setProductThumbnail(String productThumbnail) {
        this.productThumbnail = productThumbnail;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
