package com.ruoyi.system.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.system.domain.vo.WalletMsigDetailVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 多签钱包对象 tb_wallet_msig
 * 
 * @author system
 * @date 2022-09-14
 */
public class TbWalletMsig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 帐号名 */
    @Excel(name = "帐号名")
    private String name;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 帐号 */
    @Excel(name = "帐号")
    private String account;

    @Excel(name = "帐号ID")
    private String accountCode;

    @Excel(name = "发起者钱包ID")
    private Long formWalletId;

    @Excel(name = "多签帐号发起地址")
    private String formAccount;

    /** 验证用户 */
    @Excel(name = "验证用户")
    private Integer requiredUserNum;

    /** 签署用户 */
    @Excel(name = "签署用户")
    private String signers;

    private List<String> signerList;

    private String bindAccount;

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

    @Excel(name = "创建者")
    private String userName;

    private WalletMsigDetailVo detail;

    public void setId(Long id) 
    {
        this.id = id;
    }
    public Long getId() 
    {
        return id;
    }

    public void setName(String name) 
    {
        this.name = name;
    }
    public String getName() 
    {
        return name;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }
    public Long getUserId() 
    {
        return userId;
    }

    public void setAddress(String address) 
    {
        this.address = address;
    }
    public String getAddress() 
    {
        return address;
    }

    public Long getFormWalletId() {
        return formWalletId;
    }

    public void setFormWalletId(Long formWalletId) {
        this.formWalletId = formWalletId;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }
    public String getAccount() 
    {
        return account;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getFormAccount() {
        return formAccount;
    }

    public void setFormAccount(String formAccount) {
        this.formAccount = formAccount;
    }

    public void setRequiredUserNum(Integer requiredUserNum)
    {
        this.requiredUserNum = requiredUserNum;
    }
    public Integer getRequiredUserNum() 
    {
        return requiredUserNum;
    }

    public void setSigners(String signers) 
    {
        this.signers = signers;
    }
    public String getSigners() 
    {
        return signers;
    }

    public List<String> getSignerList() {
        return signerList;
    }

    public void setSignerList(List<String> signerList) {
        this.signerList = signerList;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public WalletMsigDetailVo getDetail() {
        return detail;
    }

    public void setDetail(WalletMsigDetailVo detail) {
        this.detail = detail;
    }

    public String getBindAccount() {
        return bindAccount;
    }

    public void setBindAccount(String bindAccount) {
        this.bindAccount = bindAccount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("userId", getUserId())
            .append("address", getAddress())
            .append("account", getAccount())
            .append("requiredUserNum", getRequiredUserNum())
            .append("signers", getSigners())
            .append("gmtCreate", getGmtCreate())
            .append("gmtModity", getGmtModity())
            .append("version", getVersion())
            .toString();
    }
}
