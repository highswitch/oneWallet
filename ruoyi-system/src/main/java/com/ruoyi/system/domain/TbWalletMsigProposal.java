package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 多签提案处理对象 tb_wallet_msig_proposal
 * 
 * @author system
 * @date 2022-09-20
 */
public class TbWalletMsigProposal extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 钱包ID */
    @Excel(name = "钱包ID")
    private Long walletId;

    /** 多签钱包ID */
    @Excel(name = "多签钱包ID")
    private Long walletMsigId;

    /** 任务ID */
    @Excel(name = "任务ID")
    private String taskId;

    /** 方法 */
    @Excel(name = "方法")
    private String method;

    @Excel(name = "信息ID")
    private String msgId;

    @Excel(name = "信息地址")
    private String msgUrl;

    @Excel(name = "所属人")
    private Integer isOwner;

    @Excel(name = "异常消息")
    private String errMsg;

    @Excel(name = "状态")
    private Integer status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModity;

    public TbWalletMsigProposal(){

    }
    public TbWalletMsigProposal(long userId, long walletId, long walletMsigId, String taskId,
                                String method){
        this.userId = userId;
        this.walletId = walletId;
        this.walletMsigId = walletMsigId;
        this.taskId = taskId;
        this.method = method;
        this.gmtCreate = new Date();
    }
    public void setId(Long id) 
    {
        this.id = id;
    }
    public Long getId() 
    {
        return id;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }
    public Long getUserId() 
    {
        return userId;
    }

    public void setWalletId(Long walletId) 
    {
        this.walletId = walletId;
    }
    public Long getWalletId() 
    {
        return walletId;
    }

    public void setWalletMsigId(Long walletMsigId) 
    {
        this.walletMsigId = walletMsigId;
    }
    public Long getWalletMsigId() 
    {
        return walletMsigId;
    }

    public void setTaskId(String taskId) 
    {
        this.taskId = taskId;
    }
    public String getTaskId() 
    {
        return taskId;
    }

    public void setMethod(String method) 
    {
        this.method = method;
    }
    public String getMethod() 
    {
        return method;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgUrl() {
        return msgUrl;
    }

    public void setMsgUrl(String msgUrl) {
        this.msgUrl = msgUrl;
    }

    public Integer getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(Integer isOwner) {
        this.isOwner = isOwner;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setGmtCreate(Date gmtCreate)
    {
        this.gmtCreate = gmtCreate;
    }
    public Date getGmtCreate() 
    {
        return gmtCreate;
    }

    public void setGmtModity(Date gmtModity) 
    {
        this.gmtModity = gmtModity;
    }
    public Date getGmtModity() 
    {
        return gmtModity;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("walletId", getWalletId())
            .append("walletMsigId", getWalletMsigId())
            .append("taskId", getTaskId())
            .append("method", getMethod())
            .append("gmtCreate", getGmtCreate())
            .append("gmtModity", getGmtModity())
            .toString();
    }
}
