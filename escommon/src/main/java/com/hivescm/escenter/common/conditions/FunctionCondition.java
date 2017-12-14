package com.hivescm.escenter.common.conditions;

import java.io.Serializable;

import com.hivescm.escenter.common.enums.SqlFunctionEnum;

/**
 * Created by DongChunfu on 2017/8/7
 * <p>
 * SQL 函数
 */
public class FunctionCondition implements Serializable {
    private static final int serialVersionUID = 1;

    /**
     * 字段
     */
    private String field;

    /**
     * 函数
     */
    private SqlFunctionEnum function;

    /**
     * 函数的别名，响应结果中获取响应统计结果
     */
    private String functionName;

    public FunctionCondition() {
    }

    public FunctionCondition(String field, SqlFunctionEnum function, String functionName) {
        this.field = field;
        this.function = function;
        this.functionName = functionName;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public SqlFunctionEnum getFunction() {
        return function;
    }

    public void setFunction(SqlFunctionEnum function) {
        this.function = function;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FunctionCondition{");
        sb.append("field='").append(field).append('\'');
        sb.append(", function=").append(function);
        sb.append(", functionName=").append(functionName);
        sb.append('}');
        return sb.toString();
    }
}
