package com.hivescm.disconf.config;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * 获取disconf的配置文件，启动时先把文件下载到本地，若disconf挂掉，会直接读取本地文件
 * Created by Administrator on 2017/5/15.
 */
@Service
@Scope("singleton")
@DisconfFile(filename = "config.properties")
public class DemoConfig {

    private String host;
    private int port;

    @DisconfFileItem(name = "host")
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @DisconfFileItem(name = "port")
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
