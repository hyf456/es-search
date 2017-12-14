package com.hivescm.escenter.controller;

import com.hivescm.common.domain.DataResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
public class IndexController {
    @RequestMapping(value = {"/","/info"})
    public DataResult<?>index(){
        return DataResult.success("搜索服务");
    }
}
