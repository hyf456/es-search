package com.hivescm.disconf.util;

import com.baidu.disconf.client.usertools.IDisconfDataGetter;
import com.baidu.disconf.client.usertools.impl.DisconfDataGetterDefaultImpl;

import java.util.Map;

/**
 * 增加统一的类 来个性化编程式的获取任何配置数据, 目前只支持 .properties 文件
 * Created by Administrator on 2017/5/15.
 */
public class DisconfDataGetter {

    private static IDisconfDataGetter iDisconfDataGetter = new DisconfDataGetterDefaultImpl();

    /**
     * 根据 分布式配置文件 获取该配置文件的所有数据，以 map形式呈现
     *
     * @param fileName
     *
     * @return
     */
    public static Map<String, Object> getByFile(String fileName) {
        return iDisconfDataGetter.getByFile(fileName);
    }

    /**
     * 获取 分布式配置文件 获取该配置文件 中 某个配置项 的值
     *
     * @param fileName
     * @param fileItem
     *
     * @return
     */
    public static Object getByFileItem(String fileName, String fileItem) {
        return iDisconfDataGetter.getByFileItem(fileName, fileItem);
    }

}
