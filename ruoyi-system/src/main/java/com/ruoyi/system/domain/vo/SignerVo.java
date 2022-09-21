package com.ruoyi.system.domain.vo;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignerVo {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String id;
    private String address;
    public SignerVo(){

    }
    public SignerVo(String str){
        String[] signers = str.split(" ");
        logger.info("signers:" + JSONObject.toJSONString(signers));
        this.id = signers[0];
        this.address = signers[1];
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
