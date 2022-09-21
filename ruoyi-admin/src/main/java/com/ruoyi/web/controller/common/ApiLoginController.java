package com.ruoyi.web.controller.common;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: afotc-api
 * @description: api用户登录
 * @author: ggm
 * @create: 2021-05-27 18:51
 **/
@Api("用户登录")
@RestController
@RequestMapping("/afotc/api/login")
public class ApiLoginController extends BaseController {


    @ApiOperation("测试")
    @GetMapping("/test")
    public AjaxResult test() {
        return AjaxResult.success("测试");
    }

}
