package com.hivescm.disconf.config.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.hivescm.disconf.config.DemoConfig;
import com.hivescm.disconf.config.ItemConfig;

/**
 * 当config.properties文件发生改变时，回调函数
 * Created by Administrator on 2017/5/17.
 */
@Service
@Scope("singleton")
@DisconfUpdateService(classes = { DemoConfig.class}, itemKeys = { ItemConfig.UPLODA_PAHT_KEY })
public class UpdateFileCallback implements IDisconfUpdate {
    protected static final Logger log = LoggerFactory.getLogger(UpdateFileCallback.class);

    @Override
    public void reload() throws Exception {
        log.info("此处加载完成后的操作==UpdateFileCallback=====reload============");
    }
}
