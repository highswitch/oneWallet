package com.ruoyi.common.core.domain;

import io.swagger.annotations.ApiModelProperty;

public class AjaxRet {
    @ApiModelProperty("返回码")
    private String code = "200";
    @ApiModelProperty("返回信息")
    private String msg = "操作成功";
    @ApiModelProperty("数据")
    private Object data;
    public static AjaxRet success(){
        return new AjaxRet();
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
