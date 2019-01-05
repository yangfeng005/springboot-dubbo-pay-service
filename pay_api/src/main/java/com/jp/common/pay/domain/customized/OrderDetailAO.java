package com.jp.common.pay.domain.customized;

import com.jp.common.pay.entity.gen.OrderDetail;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import java.io.Serializable;

/**
 * 应用对象 - OrderDetail.
 * <p>
 * 该类于 2018-09-27 10:53:19 首次生成，后由开发手工维护。
 * </p>
 * @author yangfeng
 * @version 1.0.0, Sep 27, 2018
 */
@JsonSerialize(include = Inclusion.ALWAYS)
public final class OrderDetailAO extends OrderDetail implements Serializable {

    /**
     * 默认的序列化 id.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 商品名称
     */
    private String productName;
    /**
     * 产品图片
     */
    private String productFileId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductFileId() {
        return productFileId;
    }

    public void setProductFileId(String productFileId) {
        this.productFileId = productFileId;
    }
}
