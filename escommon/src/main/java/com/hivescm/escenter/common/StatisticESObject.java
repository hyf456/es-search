package com.hivescm.escenter.common;

import java.io.Serializable;
import java.util.List;

import com.hivescm.escenter.common.conditions.FunctionCondition;
import com.hivescm.escenter.common.conditions.SearchCondition;

/**
 * Created by DongChunfu on 2017/8/29
 * <p>
 * 统计请求参数
 */
public class StatisticESObject extends BaseESObject implements Serializable {
    private static final long serialVersionUID = 1;

    /**
     * 过滤条件
     */
    private List<SearchCondition> searchConditions;

    /**
     * 统计函数
     */
    private List<FunctionCondition> functionConditions;

    public StatisticESObject() {
    }

    public StatisticESObject(String systemName, String indexName, String typeName) {
        super(systemName, indexName, typeName);
    }

    public List<SearchCondition> getSearchConditions() {
        return searchConditions;
    }

    public void setSearchConditions(List<SearchCondition> searchConditions) {
        this.searchConditions = searchConditions;
    }

    public List<FunctionCondition> getFunctionConditions() {
        return functionConditions;
    }

    public void setFunctionConditions(List<FunctionCondition> functionConditions) {
        this.functionConditions = functionConditions;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatisticESObject{");
        sb.append(super.toString());
        sb.append("searchConditions=").append(searchConditions);
        sb.append(", functionConditions=").append(functionConditions);
        sb.append('}');
        return sb.toString();
    }
}
