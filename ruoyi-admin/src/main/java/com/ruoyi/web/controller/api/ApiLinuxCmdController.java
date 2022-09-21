package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/linuxCmd")
public class ApiLinuxCmdController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RedisCache redisCache;
    @GetMapping("/getExecuteRet")
    public AjaxResult getExecuteRet(@RequestParam("uuid") String uuid){
        String cmdRet = redisCache.getCacheObject(uuid);
        if (!StringUtils.isNotBlank(cmdRet)){
            return AjaxResult.success("ok");
        }

        if (cmdRet.startsWith("Error:")){
            redisCache.deleteObject(uuid);
            return AjaxResult.error(cmdRet);
        }
        return AjaxResult.success(cmdRet);
    }
}
