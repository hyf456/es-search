package com.hivescm.search.index;

public enum TermVector {
	no, // 默认不存储向量信息，
	yes, // 支持term存储
	with_positions, // term+位置
	with_offsets, // （term+偏移量）
	with_positions_offsets,// term+位置+偏移量 对快速高亮fast vector
													// highlighter能提升性能，但开启又会加大索引体积，不适合大数据量用

}