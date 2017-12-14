package com.hivescm.escenter.common.conditions;

import java.io.Serializable;

import com.hivescm.escenter.common.enums.ConditionExpressionEnum;

/**
 * Created by DongChunfu on 2017/8/7
 * <p>
 * SQL 分组筛选条件
 */
public class HavingCondition implements Serializable {

	/**
	 * 字段
	 */
	private String filedName;
	/**
	 * 筛选表达式
	 */
	private ConditionExpressionEnum conditionExpressionEnum;
	/**
	 * 字段值
	 */
	private Object fieldValue;

	public HavingCondition() {
	}

	public HavingCondition(String filedName, ConditionExpressionEnum conditionExpressionEnum, Object fieldValue) {
		this.filedName = filedName;
		this.conditionExpressionEnum = conditionExpressionEnum;
		this.fieldValue = fieldValue;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("HavingCondition{");
		sb.append("filedName='").append(filedName).append('\'');
		sb.append(", conditionExpressionEnum=").append(conditionExpressionEnum);
		sb.append(", fieldValue=").append(fieldValue);
		sb.append('}');
		return sb.toString();
	}
}
