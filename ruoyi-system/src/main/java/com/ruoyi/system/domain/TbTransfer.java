package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 转帐对象 tb_transfer
 * 
 * @author system
 * @date 2022-09-14
 */
public class TbTransfer extends BaseEntity
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

    /** 发起人 */
    @Excel(name = "发起人")
    private String formAccount;

    /** 接收人 */
    @Excel(name = "接收人")
    private String toAccount;

    /** 转帐信息 */
    @Excel(name = "转帐信息")
    private BigDecimal amount;

    @Excel(name = "消息ID")
    private String messageId;

    @Excel(name = "操作人")
    private Long operationUserId;

    @Excel(name = "钱包名称")
    private String walletName;

    @Excel(name = "用户名")
    private String userName;

    @Excel(name = "操作人")
    private String operationUserName;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModity;

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

    public String getFormAccount() {
        return formAccount;
    }

    public void setFormAccount(String formAccount) {
        this.formAccount = formAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
    public BigDecimal getAmount() 
    {
        return amount;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getOperationUserId() {
        return operationUserId;
    }

    public void setOperationUserId(Long operationUserId) {
        this.operationUserId = operationUserId;
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

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOperationUserName() {
        return operationUserName;
    }

    public void setOperationUserName(String operationUserName) {
        this.operationUserName = operationUserName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("walletId", getWalletId())
            .append("form", getFormAccount())
            .append("to", getToAccount())
            .append("amount", getAmount())
            .append("gmtCreate", getGmtCreate())
            .append("gmtModity", getGmtModity())
            .toString();
    }
}
