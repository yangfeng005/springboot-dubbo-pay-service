package com.jp.common.pay.domain.customized;

import com.jp.common.pay.entity.gen.OrderEvaluationImage;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import java.io.Serializable;

/**
 * 应用对象 - OrderEvaluationImage.
 * <p>
 * 该类于 2018-10-12 15:29:28 首次生成，后由开发手工维护。
 * </p>
 * @author yangfeng
 * @version 1.0.0, Oct 12, 2018
 */
@JsonSerialize(include = Inclusion.ALWAYS)
public final class OrderEvaluationImageAO extends OrderEvaluationImage implements Serializable {

    /**
     * 默认的序列化 id.
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
