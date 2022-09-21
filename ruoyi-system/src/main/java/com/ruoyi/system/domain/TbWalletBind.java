package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户钱包绑定对象 tb_wallet_bind
 * 
 * @author system
 * @date 2022-09-19
 */
public class TbWalletBind extends BaseEntity
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

    @Excel(name = "钱包信息")
    private String walletName;

    @Excel(name = "钱包帐号信息")
    private String walletAccount;

    private String shortWalletAccount;

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

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getWalletAccount() {
        return walletAccount;
    }

    public void setWalletAccount(String walletAccount) {
        setShortAccount(walletAccount);
        this.walletAccount = walletAccount;
    }

    public String getShortWalletAccount() {
        return shortWalletAccount;
    }
    private void setShortAccount(String walletAccount){
        if (!StringUtils.isNotBlank(walletAccount)){
            return;
        }
        int le = walletAccount.length();
        String shortAccount = walletAccount.substring(0, 6) + "..." + walletAccount.substring(le - 6);
        setShortWalletAccount(shortAccount);
    }
    public void setShortWalletAccount(String shortWalletAccount) {
        this.shortWalletAccount = shortWalletAccount;
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
            .append("gmtCreate", getGmtCreate())
            .append("gmtModity", getGmtModity())
            .toString();
    }
}
