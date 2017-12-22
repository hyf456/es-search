package com.hivescm.search.index;

/**
 * elasticsearch 数据类型
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-types.html
 * 
 * @author SHOUSHEN LUAN
 */
public enum DataType {
	TEXT, KEYWORD,
	//
	DATE, IP, BOOLEAN,
	// number类型
	LONG, INTEGER, SHORT, BYTE, DOUBLE, FLOAT,
	// nested object mapping
	NESTED;

//	{
//	  "mappings": {
//	    "blogpost": {
//	      "properties": {
//	        "comments": {
//	          "type": "nested", 
//	          "properties": {
//	            "name":    { "type": "string"  },
//	            "comment": { "type": "string"  },
//	            "age":     { "type": "short"   },
//	            "stars":   { "type": "short"   },
//	            "date":    { "type": "date"    }
//	          }
//	        }
//	      }
//	    }
//	  }
//	}
}