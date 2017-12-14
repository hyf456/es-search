package com.hivescm.disconf.config;

import com.baidu.disconf.client.common.annotations.DisconfItem;
import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * 获取分布式配置项目
 * DisconfUpdateService 配置关联更新，若是UPLODA_PAHT_KEY更新，则通知更新DemoConfig会重新reload
 * Created by Administrator on 2017/5/15.
 */
@Service
@Scope("singleton")
@DisconfUpdateService(classes = { DemoConfig.class }, itemKeys = { ItemConfig.UPLODA_PAHT_KEY })
public class ItemConfig  implements IDisconfUpdate {
    protected static final Logger log = LoggerFactory.getLogger(ItemConfig.class);
    public static final String UPLODA_PAHT_KEY = "upload_path";
    private String uploadPath = "";

    /**
     * 获取配置项
     * @return
     */
    @DisconfItem(key = UPLODA_PAHT_KEY )
    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Override
    public void reload() throws Exception {
        log.info("--------------reload------------------");
    }
}
