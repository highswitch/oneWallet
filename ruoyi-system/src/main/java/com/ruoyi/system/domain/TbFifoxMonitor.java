package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * fifox监听对象 tb_fifox_monitor
 * 
 * @author ruoyi
 * @date 2022-05-13
 */
public class TbFifoxMonitor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 地址 */
    @Excel(name = "地址")
    private String url;

    /** 节点列表 */
    @Excel(name = "节点列表")
    private String nodes;

    /** 邮箱地址 */
    @Excel(name = "邮箱地址")
    private String emails;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date gmtCreate;

    /** gmt_modified */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "gmt_modified", width = 30, dateFormat = "yyyy-MM-dd")
    private Date gmtModified;
    /**
     * 节点列表
     */
    private List<String> nodeList;
    /**
     * 邮件列表
     */
    private List<String> emailList;

    /** 状态（0，不启用，1，启用） */
    @Excel(name = "状态", readConverterExp = "0=，不启用，1，启用")
    private Integer status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }
    public void setNodes(String nodes) 
    {
        this.nodes = nodes;
        if (StringUtils.isNotBlank(nodes)){
            String[] strs = nodes.split(",");
            setNodeList(Arrays.asList(strs));
        }
    }

    public String getNodes() 
    {
        return nodes;
    }
    public void setEmails(String emails) 
    {
        this.emails = emails;
        if (StringUtils.isNotBlank(emails)){
            String[] strs = emails.split(",");
            setEmailList(Arrays.asList(strs));
        }
    }

    public String getEmails() 
    {
        return emails;
    }
    public void setGmtCreate(Date gmtCreate) 
    {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtCreate() 
    {
        return gmtCreate;
    }
    public void setGmtModified(Date gmtModified) 
    {
        this.gmtModified = gmtModified;
    }

    public Date getGmtModified() 
    {
        return gmtModified;
    }

    public List<String> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<String> nodeList) {
        this.nodeList = nodeList;
    }

    public List<String> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<String> emailList) {
        this.emailList = emailList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("url", getUrl())
            .append("nodes", getNodes())
            .append("emails", getEmails())
            .append("gmtCreate", getGmtCreate())
            .append("gmtModified", getGmtModified())
            .toString();
    }
}
