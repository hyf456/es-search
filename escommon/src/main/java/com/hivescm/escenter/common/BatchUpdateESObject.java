package com.hivescm.escenter.common;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DongChunfu on 2017/8/15 批量更新请求参数
 */
public class BatchUpdateESObject implements Serializable {
	private static final long serialVersionUID = 1;

	private List<UpdateESObject> updateDatas;

	public BatchUpdateESObject() {
	}

	/**
	 * 设置是否立即刷新到磁盘
	 */
	private boolean refresh = true;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List<UpdateESObject> getUpdateDatas() {
		return updateDatas;
	}

	public void setUpdateDatas(List<UpdateESObject> updateDatas) {
		this.updateDatas = updateDatas;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("BatchUpdateESObject{");
		sb.append("updateDatas=").append(updateDatas);
		sb.append('}');
		return sb.toString();
	}

	/**
	 * 设置是否立即刷新到磁盘
	 * 
	 * @param refresh
	 */
	public BatchUpdateESObject setRefresh(boolean refresh) {
		this.refresh = refresh;
		return this;
	}

	public boolean isRefresh() {
		return refresh;
	}
}
