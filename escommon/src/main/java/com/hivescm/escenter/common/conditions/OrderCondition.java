package com.hivescm.escenter.common.conditions;

import java.io.Serializable;

import com.hivescm.escenter.common.enums.SortEnum;

/**
 * Created by DongChunfu on 2017/8/01
 * <p>
 * es 排序条件
 */
public class OrderCondition implements Serializable {
    private static final int serialVersionUID = 1;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 排序条件
     */
    private SortEnum orderCondition;


    public OrderCondition() {
    }

    public OrderCondition(String fieldName, SortEnum orderCondition) {
        this.fieldName = fieldName;
        this.orderCondition = orderCondition;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public SortEnum getOrderCondition() {
        return orderCondition;
    }

    public void setOrderCondition(SortEnum orderCondition) {
        this.orderCondition = orderCondition;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderCondition{");
        sb.append("fieldName='").append(fieldName).append('\'');
        sb.append(", orderCondition=").append(orderCondition);
        sb.append('}');
        return sb.toString();
    }
}
