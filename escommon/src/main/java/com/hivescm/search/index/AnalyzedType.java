package com.hivescm.search.index;

public enum AnalyzedType {
	ANALYZED, // 分词
	NOT_ANALYZED, // 不分词
	NO;// 设置成no，字段将不会被索引
}