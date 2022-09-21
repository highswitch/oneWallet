package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.AjaxRet;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/api/user")
public class ApiUserController {
    @Autowired
    public ISysUserService userService;
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "测试")
    })
    @ApiOperation(value = "查询用户")
    @GetMapping("/index")
    public AjaxRet index(@RequestParam("userId") long userId){
        SysUser user = userService.selectUserById(userId);
        return AjaxRet.success();
    }
}
