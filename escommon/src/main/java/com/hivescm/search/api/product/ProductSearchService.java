package com.hivescm.search.api.product;

import java.util.Map;

import com.hivescm.common.domain.DataResult;
import com.hivescm.escenter.common.ESResponse;

public interface ProductSearchService {
	/**
	 * @param name 商品名称和助记码和条码
	 * @param goodsIds 商品id集合
	 * @param goodsTags 商品的标签
	 * @param managementCategoryIds 商品类目集合
	 * @param brandIds 商品品牌集合
	 * @param storeId 用于获取商品阶梯价格
	 * @param custId 商户id
	 * @param state 商品状态默认是 30,100
	 * @Description: 查询产品索引
	 */
	DataResult<ESResponse> esInfoProductQuery(Map<String, Object> queryMap);
}
