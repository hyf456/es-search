package com.hivescm.search.index;

public enum Option {
	// 分词字段默认是position，其他的默认是docs
	docs, // 索引文档号
	freqs, // 文档号+词频
	positions, // 文档号+词频+位置，通常用来距离查询
	offsets// 文档号+词频+位置+偏移量，通常被使用在高亮字段
}