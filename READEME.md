#escenter 搜索中心

##搜索
###搜索优化
（◊） 设置超时时间，避免在查询阶段某节点占用时间较长，无法进行下一步的评分操作；

（◊） 设置路由，可知道es将相关查询路由到指定分片，避免一个索引的所有分片均要查询；
 
##更新
###单文档更新
1 单文档单一属性或引用更新（√）

（◊） 单文档引用多值属性更新（◊）

（◊） 批量更新，约定 + 配置 （◊）

##索引管理
1 索引

##分析管理
√ 设置冗余域，设置不同的分析器，保证精准的检索结果；
√ 语言识别，分析器的自定义（词干过滤器，停顿次等）；

### _all
1 一个把其它字段值当作一个大字符串来索引的特殊字段。

PUT /my_index/_mapping/my_type
{
    "my_type": {
        "_all": { 
            "enabled": false ,
            "analyzer": "whitespace"
         }
        "include_in_all": false,
        "properties": {
            "title": {
                "type": "string",
                "include_in_all": true
            }
    }
}


    

