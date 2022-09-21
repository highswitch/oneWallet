package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 钱包对象 tb_wallet
 * 
 * @author system
 * @date 2022-09-08
 */
public class TbWallet extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 帐号名 */
    private String name;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    @Excel(name = "用户名")
    private String userName;

    /** 配置文件 */
    @Excel(name = "配置文件")
    private String conf;

    /** Index */
    @Excel(name = "walletIndex")
    private Long walletIndex;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 帐号 */
    @Excel(name = "帐号")
    private String account;

    @Excel(name = "类型")
    private String type;

    @Excel(name = "余额")
    private String balance;

    @Excel(name = "货币类型")
    private String currency;

    @Excel(name = "提醒消息")
    private String warningTip;

    @Excel(name = "是否私匙")
    private int isPrivate;

    private String privateKey;

    private String password;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModity;

    /** 版本 */
    @Excel(name = "版本")
    private Long version;

    private List<String> mnemonic;

    public void setId(Long id) 
    {
        this.id = id;
    }
    public Long getId() 
    {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }
    public Long getUserId() 
    {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setConf(String conf)
    {
        this.conf = conf;
    }
    public String getConf() 
    {
        return conf;
    }

    public Long getWalletIndex() {
        return walletIndex;
    }

    public void setWalletIndex(Long walletIndex) {
        this.walletIndex = walletIndex;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }
    public String getAddress() 
    {
        return address;
    }

    public void setAccount(String account) 
    {
        this.account = account;
    }
    public String getAccount() 
    {
        return account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getWarningTip() {
        return warningTip;
    }

    public void setWarningTip(String warningTip) {
        this.warningTip = warningTip;
    }

    public int getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(int isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setVersion(Long version) 
    {
        this.version = version;
    }
    public Long getVersion() 
    {
        return version;
    }

    public List<String> getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(List<String> mnemonic) {
        this.mnemonic = mnemonic;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("conf", getConf())
            .append("index", getWalletIndex())
            .append("address", getAddress())
            .append("account", getAccount())
            .append("gmtCreate", getGmtCreate())
            .append("gmtModity", getGmtModity())
            .append("version", getVersion())
            .toString();
    }
}
