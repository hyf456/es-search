package com.hivescm.escenter.common;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DongChunfu on 2017/8/16
 * 批量删除请求参数
 */
public class BatchDeleteESObject implements Serializable {
	private static final long serialVersionUID = 1;

	private List<DeleteESObject> deleteDatas;
	/**
	 * 设置是否立即刷新到磁盘
	 */
	private boolean refresh = true;
	public BatchDeleteESObject() {
	}

	public List<DeleteESObject> getDeleteDatas() {
		return deleteDatas;
	}

	public void setDeleteDatas(List<DeleteESObject> deleteDatas) {
		this.deleteDatas = deleteDatas;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("BatchDeleteESObject{");
		sb.append("deleteDatas=").append(deleteDatas);
		sb.append('}');
		return sb.toString();
	}

	public boolean isRefresh() {
		return refresh;
	}

	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}
}
