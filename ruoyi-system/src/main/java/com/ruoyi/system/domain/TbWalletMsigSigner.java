package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 多签钱包签署人对象 tb_wallet_msig_signer
 * 
 * @author system
 * @date 2022-09-15
 */
public class TbWalletMsigSigner extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 多签钱包ID */
    @Excel(name = "多签钱包ID")
    private Long walletMsigId;

    /** 钱包ID */
    @Excel(name = "钱包ID")
    private Long walletId;

    @Excel(name = "帐号")
    private String account;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

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

    public void setWalletMsigId(Long walletMsigId) 
    {
        this.walletMsigId = walletMsigId;
    }
    public Long getWalletMsigId() 
    {
        return walletMsigId;
    }

    public void setWalletId(Long walletId) 
    {
        this.walletId = walletId;
    }
    public Long getWalletId() 
    {
        return walletId;
    }

    public void setGmtCreate(Date gmtCreate) 
    {
        this.gmtCreate = gmtCreate;
    }
    public Date getGmtCreate() 
    {
        return gmtCreate;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("walletMsigId", getWalletMsigId())
            .append("walletId", getWalletId())
            .append("gmtCreate", getGmtCreate())
            .toString();
    }
}
